package com.dpm.winwin.api.post.dto.response;

import com.dpm.winwin.domain.entity.post.Post;

import java.util.List;
import java.util.stream.Collectors;

public record PostAddResponse(
        long id,
        String title,
        String content,
        boolean isShare,
        String mainCategory,
        String midCategory,
        String subCategory,
        List<LinkResponse> links,
        String chatLink,
        List<String> takenCategories,
        String takenContent,
        String exchangeType,
        String exchangePeriod,
        String exchangeTime
) {
    public static PostAddResponse from(Post savePost) {
        return new PostAddResponse(
                savePost.getId(),
                savePost.getTitle(),
                savePost.getContent(),
                savePost.isShare(),
                savePost.getMainCategory().getName(),
                savePost.getMidCategory().getName(),
                savePost.getSubCategory().getName(),
                savePost.getLinks().stream()
                        .map(LinkResponse::of)
                        .collect(Collectors.toList()),
                savePost.getChatLink(),
                savePost.getTakenTalents().stream()
                        .map(t -> t.getTalent().getName())
                        .toList(),
                savePost.getTakenContent(),
                savePost.getExchangeType().getMessage(),
                savePost.getExchangePeriod().getMessage(),
                savePost.getExchangeTime().getMessage()
        );
    }
}
