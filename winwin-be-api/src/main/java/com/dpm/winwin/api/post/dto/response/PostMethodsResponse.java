package com.dpm.winwin.api.post.dto.response;

import java.util.List;

public record PostMethodsResponse(
    List<PostMethodResponse> exchangeTypes,
    List<PostMethodResponse> exchangePeriods,
    List<PostMethodResponse> exchangeTimes
) {

    public static PostMethodsResponse of(
        List<PostMethodResponse> exchangeTypes,
        List<PostMethodResponse> exchangePeriods,
        List<PostMethodResponse> exchangeTimes
    ) {
        return new PostMethodsResponse(exchangeTypes, exchangePeriods, exchangeTimes);
    }
}
