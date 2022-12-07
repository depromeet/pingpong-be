package com.dpm.winwin.api.post.service;

import com.dpm.winwin.api.common.error.enums.ErrorMessage;
import com.dpm.winwin.api.common.error.exception.custom.BusinessException;
import com.dpm.winwin.api.common.response.dto.GlobalPageResponseDto;
import com.dpm.winwin.api.post.dto.request.LinkRequest;
import com.dpm.winwin.api.post.dto.request.PostAddRequest;
import com.dpm.winwin.api.post.dto.request.PostUpdateRequest;
import com.dpm.winwin.api.post.dto.response.LinkResponse;
import com.dpm.winwin.api.post.dto.response.MyPagePostListResponse;
import com.dpm.winwin.api.post.dto.response.MyPagePostResponse;
import com.dpm.winwin.api.post.dto.response.PostAddResponse;
import com.dpm.winwin.api.post.dto.response.PostCustomizedResponse;
import com.dpm.winwin.api.post.dto.response.PostListResponse;
import com.dpm.winwin.api.post.dto.response.PostMethodResponse;
import com.dpm.winwin.api.post.dto.response.PostMethodsResponse;
import com.dpm.winwin.api.post.dto.response.PostReadResponse;
import com.dpm.winwin.api.post.dto.response.PostUpdateResponse;
import com.dpm.winwin.domain.entity.category.MainCategory;
import com.dpm.winwin.domain.entity.category.MidCategory;
import com.dpm.winwin.domain.entity.category.SubCategory;
import com.dpm.winwin.domain.entity.link.Link;
import com.dpm.winwin.domain.entity.member.Member;
import com.dpm.winwin.domain.entity.post.Post;
import com.dpm.winwin.domain.entity.post.PostTalent;
import com.dpm.winwin.domain.entity.post.enums.ExchangePeriod;
import com.dpm.winwin.domain.entity.post.enums.ExchangeTime;
import com.dpm.winwin.domain.entity.post.enums.ExchangeType;
import com.dpm.winwin.domain.repository.category.MainCategoryRepository;
import com.dpm.winwin.domain.repository.category.MidCategoryRepository;
import com.dpm.winwin.domain.repository.category.SubCategoryRepository;
import com.dpm.winwin.domain.repository.link.LinkRepository;
import com.dpm.winwin.domain.repository.member.MemberRepository;
import com.dpm.winwin.domain.repository.post.PostRepository;
import com.dpm.winwin.domain.repository.post.dto.request.PostCustomizedConditionRequest;
import com.dpm.winwin.domain.repository.post.dto.request.PostListConditionRequest;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final MemberRepository memberRepository;
    private final MainCategoryRepository mainCategoryRepository;
    private final MidCategoryRepository midCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final PostRepository postRepository;
    private final LinkRepository linkRepository;

    public GlobalPageResponseDto<PostListResponse> getPosts(PostListConditionRequest condition, Pageable pageable) {
        Page<PostListResponse> page = postRepository
            .getAllByIsShareAndMidCategory(condition, pageable)
            .map(PostListResponse::of);
        return GlobalPageResponseDto.of(page);
    }

    public GlobalPageResponseDto<PostCustomizedResponse> getPostsCustomized(
        Long memberId, PostCustomizedConditionRequest condition, Pageable pageable) {
        Page<PostCustomizedResponse> page = postRepository
            .getAllByMemberTalents(memberId, condition, pageable)
            .map(PostCustomizedResponse::of);
        return GlobalPageResponseDto.of(page);
    }

    public PostAddResponse save(long memberId, PostAddRequest request) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(ErrorMessage.MEMBER_NOT_FOUND));
        MainCategory mainCategory = mainCategoryRepository.findById(request.mainCategoryId())
            .orElseThrow(() -> new BusinessException(ErrorMessage.MAIN_CATEGORY_NOT_FOUND));
        MidCategory midCategory = midCategoryRepository.findById(request.midCategoryId())
            .orElseThrow(() -> new BusinessException(ErrorMessage.MID_CATEGORY_NOT_FOUND));
        SubCategory subCategory = subCategoryRepository.findById(request.subCategoryId())
            .orElseThrow(() -> new BusinessException(ErrorMessage.SUB_CATEGORY_NOT_FOUND));

        List<Link> links = request.links().stream()
            .map(Link::of)
            .toList();

        Post post = request.toEntity();
        post.writeBy(member);
        post.setAllCategory(mainCategory, midCategory, subCategory);
        post.setLink(links);
        post.setTakenContent(request.takenContent());

        List<PostTalent> postTalents = request.takenTalentIds().stream()
            .map(talentId -> PostTalent.of(post, subCategoryRepository.findById(talentId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.SUB_CATEGORY_NOT_FOUND))))
            .toList();

        postTalents.forEach(post::addTakenTalent);

        Post savedPost = postRepository.save(post);
        return PostAddResponse.from(savedPost);
    }

    @Transactional(readOnly = true)
    public PostReadResponse get(Long postId, Long memberId) {
        Post post = postRepository.getByIdFetchJoin(postId)
            .orElseThrow(() -> new BusinessException(ErrorMessage.POST_NOT_FOUND));

        Boolean isLike = postRepository.hasLikeByMemberId(postId, memberId);

        return PostReadResponse.from(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            post.isShare(),
            post.getSubCategory().getName(),
            post.getLinks().stream().map(LinkResponse::of).toList(),
            post.getChatLink(),
            post.getLikes().size(),
            post.getExchangeType().getMessage(),
            post.getExchangePeriod().getMessage(),
            post.getExchangeTime().getMessage(),
            post.getTakenContent(),
            post.getTakenTalents().stream()
                .map(postTalent -> postTalent.getTalent().getName())
                .toList(),
            post.getMember().getId(),
            post.getMember().getNickname(),
            post.getMember().getImage(),
            post.getMember().getRanks().getName(),
            isLike);
    }

    public Long delete(Long id) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorMessage.POST_NOT_FOUND));

        postRepository.delete(post);
        return post.getId();
    }

    public Post getByIdAndMemberId(long memberId, Long id) {
        return postRepository.getByIdAndMemberId(memberId, id)
            .orElseThrow(() -> new BusinessException(ErrorMessage.POST_NOT_FOUND));
    }

    public PostUpdateResponse update(Long memberId, Long postId,
        PostUpdateRequest updateRequest) {
        Post post = getByIdAndMemberId(memberId, postId);
        MainCategory mainCategory = mainCategoryRepository.findById(updateRequest.mainCategoryId())
            .orElseThrow(() -> new BusinessException(ErrorMessage.MAIN_CATEGORY_NOT_FOUND));
        MidCategory midCategory = midCategoryRepository.findById(updateRequest.midCategoryId())
            .orElseThrow(() -> new BusinessException(ErrorMessage.MID_CATEGORY_NOT_FOUND));
        SubCategory subCategory = subCategoryRepository.findById(updateRequest.subCategoryId())
            .orElseThrow(() -> new BusinessException(ErrorMessage.SUB_CATEGORY_NOT_FOUND));
        List<SubCategory> savedTalents = subCategoryRepository.findAllById(
            updateRequest.takenTalents());

        post.update(updateRequest.toDto(), mainCategory, midCategory, subCategory, savedTalents);

        for (LinkRequest linkRequest : updateRequest.getExistentLinks()) {
            Link link = linkRepository.findById(linkRequest.id())
                .orElseThrow(() -> new BusinessException(ErrorMessage.LINK_NOT_FOUND));
            link.setContent(linkRequest.content());
        }

        return new PostUpdateResponse(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            post.isShare(),
            post.getMainCategory().getName(),
            post.getMidCategory().getName(),
            post.getSubCategory().getName(),
            post.getLinks().stream()
                .map(LinkResponse::of)
                .toList(),
            post.getChatLink(),
            post.getTakenTalents().stream()
                .map(postTalent -> postTalent.getTalent().getName())
                .toList(),
            post.getTakenContent(),
            post.getExchangeType().getMessage(),
            post.getExchangePeriod().getMessage(),
            post.getExchangeTime().getMessage()
        );
    }

    public PostMethodsResponse getPostMethod() {
        List<PostMethodResponse> exchangeTypes = Arrays.stream(ExchangeType.values())
            .map(type -> PostMethodResponse.of(type.name(), type.getMessage()))
            .toList();

        List<PostMethodResponse> exchangePeriods = Arrays.stream(ExchangePeriod.values())
            .map(period -> PostMethodResponse.of(period.name(), period.getMessage()))
            .toList();

        List<PostMethodResponse> exchangeTimes = Arrays.stream(ExchangeTime.values())
            .map(time -> PostMethodResponse.of(time.name(), time.getMessage()))
            .toList();

        return PostMethodsResponse.of(exchangeTypes, exchangePeriods, exchangeTimes);
    }

    public GlobalPageResponseDto<MyPagePostResponse> getAllByMemberId(Long memberId, Pageable pageable) {
        Page<MyPagePostResponse> page = postRepository.getAllByMemberId(memberId, pageable)
            .map(MyPagePostResponse::of);

        return GlobalPageResponseDto.of(page);
    }
}
