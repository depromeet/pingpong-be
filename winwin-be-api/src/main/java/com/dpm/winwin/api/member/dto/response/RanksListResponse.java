package com.dpm.winwin.api.member.dto.response;

import java.util.List;

public record RanksListResponse(
    List<RanksResponse> ranks
) {

    public static RanksListResponse from(List<RanksResponse> ranks) {
        return new RanksListResponse(ranks);
    }
}
