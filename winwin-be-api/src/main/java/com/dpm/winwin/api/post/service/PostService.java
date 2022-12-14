package com.dpm.winwin.api.post.service;

import static com.dpm.winwin.api.common.error.enums.ErrorMessage.INVALID_POST_REQUEST;

import com.dpm.winwin.api.common.error.enums.ErrorMessage;
import com.dpm.winwin.api.common.error.exception.custom.BusinessException;
import com.dpm.winwin.api.common.response.dto.GlobalPageResponseDto;
import com.dpm.winwin.api.post.dto.request.LinkRequest;
import com.dpm.winwin.api.post.dto.request.PostAddRequest;
import com.dpm.winwin.api.post.dto.request.PostUpdateRequest;
import com.dpm.winwin.api.post.dto.response.LinkResponse;
import com.dpm.winwin.api.post.dto.response.MyPagePostResponse;
import com.dpm.winwin.api.post.dto.response.PostAddResponse;
import com.dpm.winwin.api.post.dto.response.PostCustomizedResponse;
import com.dpm.winwin.api.post.dto.response.PostResponse;
import com.dpm.winwin.api.post.dto.response.PostMethodResponse;
import com.dpm.winwin.api.post.dto.response.PostMethodsResponse;
import com.dpm.winwin.api.post.dto.response.PostReadResponse;
import com.dpm.winwin.api.post.dto.response.PostUpdateResponse;
import com.dpm.winwin.domain.entity.category.SubCategory;
import com.dpm.winwin.domain.entity.link.Link;
import com.dpm.winwin.domain.entity.member.Member;
import com.dpm.winwin.domain.entity.post.Post;
import com.dpm.winwin.domain.entity.post.PostTalent;
import com.dpm.winwin.domain.entity.post.enums.ExchangePeriod;
import com.dpm.winwin.domain.entity.post.enums.ExchangeTime;
import com.dpm.winwin.domain.entity.post.enums.ExchangeType;
import com.dpm.winwin.domain.repository.category.MainCategoryRepository;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final MemberRepository memberRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final PostRepository postRepository;
    private final LinkRepository linkRepository;

    public GlobalPageResponseDto<PostResponse> getPosts(Long memberId,
                                                        PostListConditionRequest condition,
                                                        Pageable pageable) {
        Page<PostResponse> page = postRepository
            .getAllByIsShareAndCategory(memberId, condition, pageable)
            .map(PostResponse::of);
        return GlobalPageResponseDto.of(page);
    }

    public GlobalPageResponseDto<PostCustomizedResponse> getPostsCustomized(
        Long memberId, PostCustomizedConditionRequest condition, Pageable pageable) {
        Page<PostCustomizedResponse> page = postRepository
            .getAllByMemberTalents(memberId, condition, pageable)
            .map(PostCustomizedResponse::of);
        return GlobalPageResponseDto.of(page);
    }

    public PostAddResponse save(Long memberId, PostAddRequest request) {
        if (!validateRequestByIsShare(request.isShare(), request.takenTalentIds(), request.takenContent())) {
            throw new BusinessException(INVALID_POST_REQUEST);
        }

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(ErrorMessage.MEMBER_NOT_FOUND));
        SubCategory subCategory = subCategoryRepository
            .getByIdWithMainCategoryAndMidCategory(request.subCategoryId())
            .orElseThrow(() -> new BusinessException(ErrorMessage.SUB_CATEGORY_NOT_FOUND));

        Post post = request.toEntity();
        post.writeBy(member);
        post.setAllCategoriesBySubCategory(subCategory);
        post.setLink(request.links());

        if (!request.isShare()) {
            post.setTakenContent(request.takenContent());
            List<PostTalent> postTalents = request.takenTalentIds().stream()
                .map(talentId -> PostTalent.of(post, subCategoryRepository.findById(talentId)
                    .orElseThrow(() -> new BusinessException(ErrorMessage.SUB_CATEGORY_NOT_FOUND))))
                .toList();

            postTalents.forEach(post::addTakenTalent);
        }
        Post savedPost = postRepository.save(post);
        return PostAddResponse.from(savedPost);
    }

    @Transactional(readOnly = true)
    public PostReadResponse get(Long postId, Long memberId) {
        Post post = postRepository.getByIdFetchJoin(postId)
            .orElseThrow(() -> new BusinessException(ErrorMessage.POST_NOT_FOUND));

        Boolean hasLike = postRepository.hasLikeByMemberId(postId, memberId);

        return PostReadResponse.from(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            post.isShare(),
            post.getSubCategory().getName(),
            post.getLinks().stream().map(LinkResponse::of).toList(),
            post.getChatLink(),
            post.getLikes().size(),
            post.getTakenContent(), post.getTakenTalents().stream()
                .map(postTalent -> postTalent.getTalent().getName())
                .toList(), post.getExchangeType(),
            post.getExchangePeriod(),
            post.getExchangeTime(),
            post.getMember().getId(),
            post.getMember().getNickname(),
            post.getMember().getImage(),
            post.getMember().getRanks().getName(),
            hasLike,
            post.getMainCategory().getBackgroundImage());
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
        if (!validateRequestByIsShare(updateRequest.isShare(), updateRequest.takenTalents(),
            updateRequest.takenContent())) {
            throw new BusinessException(INVALID_POST_REQUEST);
        }

        Post post = getByIdAndMemberId(memberId, postId);
        SubCategory subCategory = subCategoryRepository
            .getByIdWithMainCategoryAndMidCategory(updateRequest.subCategoryId())
            .orElseThrow(() -> new BusinessException(ErrorMessage.SUB_CATEGORY_NOT_FOUND));
        List<SubCategory> savedTalents = subCategoryRepository.findAllById(
            updateRequest.takenTalents());

        post.update(updateRequest.toDto(), subCategory, savedTalents);

        for (LinkRequest linkRequest : updateRequest.filterExistentLinks()) {
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
            post.getExchangeType(),
            post.getExchangePeriod(),
            post.getExchangeTime()
        );
    }

    public PostMethodsResponse getPostMethods() {
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

    private boolean validateRequestByIsShare(boolean isShare, List<Long> takenTalentIds, String takenContent) {
        if (isShare) {
            return !StringUtils.hasText(takenContent) && CollectionUtils.isEmpty(takenTalentIds);
        }
        return StringUtils.hasText(takenContent) && !CollectionUtils.isEmpty(takenTalentIds);
    }
}
