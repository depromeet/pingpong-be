package com.dpm.winwin.api.post.dto.response;

import java.util.List;

public record PostReadResponse(Long id, String title, String content, boolean isShare,
                               String subCategory, List<LinkResponse> links, String chatLink, int likes,
                               String exchangeType, String exchangePeriod, String exchangeTime, String takenContent,
                               List<String> takenTalents, Long memberId, String nickname, String image, String ranks,
                               Boolean isLike) {

    public static PostReadResponse from(Long id, String title, String content, boolean isShare,
        String subCategory, List<LinkResponse> links, String chatLink, int likes, String exchangeType,
        String exchangePeriod, String exchangeTime, String takenContent,
        List<String> takenTalents, Long memberId, String nickname, String image, String ranks, Boolean isLike) {
        return new PostReadResponse(id, title, content, isShare, subCategory, links, chatLink, likes, exchangeType,
            exchangePeriod, exchangeTime, takenContent, takenTalents, memberId, nickname, image, ranks, isLike);
    }
}
