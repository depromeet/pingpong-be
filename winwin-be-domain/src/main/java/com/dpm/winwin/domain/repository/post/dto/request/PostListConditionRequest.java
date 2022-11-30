package com.dpm.winwin.domain.repository.post.dto.request;

public record PostListConditionRequest(
    Boolean isShare,
    Long mainCategory,
    Long midCategory,
    Long subCategory
) {

}
