package com.dpm.winwin.api.post.dto.response;

import com.dpm.winwin.domain.entity.post.Post;

public record LikesResponse(
        int likes
) {

    public static LikesResponse from(Post post) {
        return new LikesResponse(
                post.getLikes().size()
        );
    }
}
