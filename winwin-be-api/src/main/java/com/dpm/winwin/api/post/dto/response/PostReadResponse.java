package com.dpm.winwin.api.post.dto.response;

import java.util.List;

public record PostReadResponse(Long id, String title, String content, boolean isShare,
                               String mainCategory, String midCategory, String subCategory,
                               List<LinkResponse> links, String chatLink, int likes,
                               String exchangeType, String exchangePeriod, String exchangeTime) {

    public static PostReadResponse from(Long id, String title, String content, boolean isShare,
        String mainCategory, String midCategory, String subCategory,
        List<LinkResponse> links, String chatLink, int likes, String exchangeType,
        String exchangePeriod, String exchangeTime) {
        return new PostReadResponse(id, title, content, isShare, mainCategory, midCategory,
            subCategory, links, chatLink, likes, exchangeType, exchangePeriod,
            exchangeTime);
    }
}
