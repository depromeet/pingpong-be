package com.dpm.winwin.api.post.dto.response;

import java.util.List;

public record PostUpdateResponse(
    long id,
    String title,
    String content,
    boolean isShare,
    String mainCategoryId,
    String midCategoryId,
    String subCategoryId,
    List<LinkResponse> links,
    String chatLink,
    List<String> takenCategories,
    String takenContent,
    String exchangeType,
    String exchangePeriod,
    String exchangeTime
) {

}
