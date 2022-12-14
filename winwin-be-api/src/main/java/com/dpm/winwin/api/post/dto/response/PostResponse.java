package com.dpm.winwin.api.post.dto.response;

import com.dpm.winwin.domain.entity.post.Post;
import java.util.List;

public record PostResponse(
    Long id,
    String title,
    String subCategory,
    boolean isShare,
    Integer likes,
    Long memberId,
    String nickname,
    String image,
    String ranks,
    List<String> takenTalents
) {

    public static PostResponse of(Post post) {
        return new PostResponse(
            post.getId(),
            post.getTitle(),
            post.getSubCategory().getName(),
            post.isShare(),
            post.getLikes().size(),
            post.getMember().getId(),
            post.getMember().getNickname(),
            post.getMember().getImage(),
            post.getMember().getRanks().getName(),
            post.getTakenTalents().stream()
                .map(postTalent -> postTalent.getTalent().getName())
                .toList());
    }
}
