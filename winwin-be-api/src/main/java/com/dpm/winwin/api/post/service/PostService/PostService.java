package com.dpm.winwin.api.post.service.PostService;

import com.dpm.winwin.api.common.error.enums.ErrorMessage;
import com.dpm.winwin.api.common.error.exception.custom.BusinessException;
import com.dpm.winwin.api.post.dto.request.PostAddRequest;
import com.dpm.winwin.api.post.dto.response.PostAddResponse;
import com.dpm.winwin.domain.entity.category.MainCategory;
import com.dpm.winwin.domain.entity.category.MidCategory;
import com.dpm.winwin.domain.entity.category.SubCategory;
import com.dpm.winwin.domain.entity.link.Link;
import com.dpm.winwin.domain.entity.member.Member;
import com.dpm.winwin.domain.entity.post.Post;
import com.dpm.winwin.domain.repository.category.MainCategoryRepository;
import com.dpm.winwin.domain.repository.category.MidCategoryRepository;
import com.dpm.winwin.domain.repository.category.SubCategoryRepository;
import com.dpm.winwin.domain.repository.member.MemberRepository;
import com.dpm.winwin.domain.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
                .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_MEMBER));
        MainCategory mainCategory = mainCategoryRepository.findById(request.mainCategoryId())
                .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_MAIN_CATEGORY));
        MidCategory midCategory = midCategoryRepository.findById(request.midCategoryId())
                .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_MID_CATEGORY));
        SubCategory subCategory = subCategoryRepository.findById(request.subCategoryId())
                .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_SUB_CATEGORY));

        List<Link> linkList = request.links().stream()
                .map(Link::of)
                .collect(Collectors.toList());

        Post post = request.toEntity();
        post.writeBy(member);
        post.setAllCategory(mainCategory, midCategory, subCategory);
        post.setLink(linkList);

        Post savedPost = postRepository.save(post);
        return PostAddResponse.from(savedPost);
    }
}
