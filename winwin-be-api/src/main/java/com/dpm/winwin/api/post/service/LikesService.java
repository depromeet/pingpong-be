package com.dpm.winwin.api.post.service;

import com.dpm.winwin.api.common.error.enums.ErrorMessage;
import com.dpm.winwin.api.common.error.exception.custom.BusinessException;
import com.dpm.winwin.api.post.dto.request.LikeAddRequest;
import com.dpm.winwin.api.post.dto.response.LikesResponse;
import com.dpm.winwin.domain.entity.member.Member;
import com.dpm.winwin.domain.entity.post.Likes;
import com.dpm.winwin.domain.entity.post.Post;
import com.dpm.winwin.domain.repository.member.MemberRepository;
import com.dpm.winwin.domain.repository.post.LikesRepository;
import com.dpm.winwin.domain.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikesService {
    private final LikesRepository likesRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public LikesResponse createLikes(Long memberId, Long postId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.MEMBER_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.POST_NOT_FOUND));

        if(!isAlreadyLike(member, post)) {
            LikeAddRequest likeAddRequest = new LikeAddRequest(member, post);
            likesRepository.save(likeAddRequest.toEntity());
            post.getMember().plusTotalPostLike();
        }

        return LikesResponse.from(post);
    }

    public LikesResponse cancelLikes(Long memberId, Long postId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.MEMBER_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.POST_NOT_FOUND));

        if(isAlreadyLike(member, post)) {
            Likes likes = likesRepository.findByMemberAndPost(member, post)
                    .orElseThrow(() -> new BusinessException(ErrorMessage.LIKE_NOT_FOUND));;
            likesRepository.delete(likes);
            post.minusLikes(likes);
            post.getMember().minusTotalPostLike();
        }

        return LikesResponse.from(post);
    }

    private boolean isAlreadyLike(Member member, Post post) {
        return likesRepository.findByMemberAndPost(member, post).isPresent();
    }
}
