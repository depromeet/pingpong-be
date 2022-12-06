package com.dpm.winwin.api.post.dto.response;

import com.dpm.winwin.domain.entity.post.Post;
import java.util.List;

public record PostListResponse(
    Long id,
    String title,
    String subCategory,
    Integer likes,
    String member,
    String image,
    String ranks,
    List<String> takenTalents
) {

    public static PostListResponse of(Post post) {
        return new PostListResponse(
            post.getId(),
            post.getTitle(),
            post.getSubCategory().getName(),
            post.getLikes().size(),
            post.getMember().getNickname(),
            post.getMember().getImage(),
            post.getMember().getRanks().getName(),
            post.getTakenTalents().stream()
                .map(postTalent -> postTalent.getTalent().getName())
                .toList());
    }
}
