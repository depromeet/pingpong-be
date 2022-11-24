package com.dpm.winwin.api.post.service;

import com.dpm.winwin.api.common.error.enums.ErrorMessage;
import com.dpm.winwin.api.common.error.exception.custom.BusinessException;
import com.dpm.winwin.api.post.dto.request.PostAddRequest;
import com.dpm.winwin.api.post.dto.response.LinkResponse;
import com.dpm.winwin.api.post.dto.response.PostAddResponse;
import com.dpm.winwin.api.post.dto.response.PostReadResponse;
import com.dpm.winwin.domain.entity.category.MainCategory;
import com.dpm.winwin.domain.entity.category.MidCategory;
import com.dpm.winwin.domain.entity.category.SubCategory;
import com.dpm.winwin.domain.entity.link.Link;
import com.dpm.winwin.domain.entity.member.Member;
import com.dpm.winwin.domain.entity.post.Post;
import com.dpm.winwin.domain.entity.post.PostTalent;
import com.dpm.winwin.domain.repository.category.MainCategoryRepository;
import com.dpm.winwin.domain.repository.category.MidCategoryRepository;
import com.dpm.winwin.domain.repository.category.SubCategoryRepository;
import com.dpm.winwin.domain.repository.member.MemberRepository;
import com.dpm.winwin.domain.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final MemberRepository memberRepository;
    private final MainCategoryRepository mainCategoryRepository;
    private final MidCategoryRepository midCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final PostRepository postRepository;

    public PostAddResponse save(long memberId, PostAddRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.MEMBER_NOT_FOUND));
        MainCategory mainCategory = mainCategoryRepository.findById(request.mainCategory())
                .orElseThrow(() -> new BusinessException(ErrorMessage.MAIN_CATEGORY_NOT_FOUND));
        MidCategory midCategory = midCategoryRepository.findById(request.midCategory())
                .orElseThrow(() -> new BusinessException(ErrorMessage.MID_CATEGORY_NOT_FOUND));
        SubCategory subCategory = subCategoryRepository.findById(request.subCategory())
                .orElseThrow(() -> new BusinessException(ErrorMessage.SUB_CATEGORY_NOT_FOUND));

        List<Link> linkList = request.links().stream()
                .map(Link::of)
                .toList();

        Post post = request.toEntity();
        post.writeBy(member);
        post.setAllCategory(mainCategory, midCategory, subCategory);
        post.setLink(linkList);
        post.setTakenContent(request.takenContent());

        List<PostTalent> postTalents = request.takenCategories().stream()
                .map(t -> PostTalent.of(post, subCategoryRepository.findById(t)
                        .orElseThrow(() -> new BusinessException(ErrorMessage.SUB_CATEGORY_NOT_FOUND))))
                .toList();

        for (PostTalent postTalent : postTalents) {
            post.addTakenTalent(postTalent);
        }

        Post savedPost = postRepository.save(post);
        return PostAddResponse.from(savedPost);
    }

  @Transactional(readOnly = true)
  public PostReadResponse get(Long id) {
      Post post = postRepository.findById(id)
              .orElseThrow(() -> new BusinessException(ErrorMessage.POST_NOT_FOUND));
      return PostReadResponse.from(
              post.getId(),
              post.getTitle(),
              post.getContent(),
              post.isShare(),
              post.getMainCategory().getName(),
              post.getMidCategory().getName(),
              post.getSubCategory().getName(),
              post.getLinks().stream().map(LinkResponse::of).toList(),
              post.getLikes().size(),
              post.getExchangeType().getMessage(),
              post.getExchangePeriod().getMessage(),
              post.getExchangeTime().getMessage());
  }

    public Long delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessage.POST_NOT_FOUND));

        postRepository.delete(post);
        return post.getId();
    }
}
