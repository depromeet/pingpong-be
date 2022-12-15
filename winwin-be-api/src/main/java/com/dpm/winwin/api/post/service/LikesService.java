package com.dpm.winwin.api.post.service;

import com.dpm.winwin.api.common.error.enums.ErrorMessage;
import com.dpm.winwin.api.common.error.exception.custom.BusinessException;
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

    public boolean createLike(Long memberId, Long postId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.MEMBER_NOT_FOUND));
        Post post = postRepository.getByIdFetchJoin(postId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.POST_NOT_FOUND));

        if(isNotAlreadyLike(member, post)){
            likesRepository.save(new Likes(member, post));
            return true;
        }
        return false;
    }

    private boolean isNotAlreadyLike(Member member, Post post) {
        return likesRepository.findByMemberAndPost(member, post).isEmpty();
    }
}
