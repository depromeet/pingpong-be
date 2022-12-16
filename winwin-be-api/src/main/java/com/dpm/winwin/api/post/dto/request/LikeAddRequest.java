package com.dpm.winwin.api.post.dto.request;

import com.dpm.winwin.domain.entity.member.Member;
import com.dpm.winwin.domain.entity.post.Likes;
import com.dpm.winwin.domain.entity.post.Post;

public record LikeAddRequest(
        Member member,
        Post post
) {
    public Likes toEntity() {
        return Likes.builder()
                .member(this.member)
                .post(this.post)
                .build();
    }
}
