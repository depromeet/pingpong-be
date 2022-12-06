package com.dpm.winwin.api.post.dto.response;

import com.dpm.winwin.domain.entity.post.Post;

public record PostCustomizedResponse(
    Long postId,
    String title,
    String subCategory,
    Integer likes,
    Long memberId,
    String nickname,
    String image,
    String ranks
) {

    public static PostCustomizedResponse of(Post post) {
        return new PostCustomizedResponse(
            post.getId(),
            post.getTitle(),
            post.getSubCategory().getName(),
            post.getLikes().size(),
            post.getMember().getId(),
            post.getMember().getNickname(),
            post.getMember().getImage(),
            post.getMember().getRanks().getName());
    }
}
