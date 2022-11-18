package com.dpm.winwin.api.post.dto.response;

import java.util.List;

public record PostReadResponse(Long id, String title, String content, boolean isShare,
                               String mainCategoryId, String midCategoryId, String subCategoryId,
                               List<LinkResponse> responses, String exchangeType, String exchangePeriod,
                               String exchangeTime) {

    public static PostReadResponse from(Long id, String title, String content, boolean isShare,
        String mainCategoryId, String midCategoryId, String subCategoryId,
        List<LinkResponse> linkResponses, String exchangeType, String exchangePeriod,
        String exchangeTime) {
        return new PostReadResponse(id, title, content, isShare, mainCategoryId, midCategoryId,
            subCategoryId, linkResponses, exchangeType, exchangePeriod, exchangeTime);
    }
}
