package com.dpm.winwin.api.member.dto.response;

import com.dpm.winwin.domain.repository.member.dto.response.MemberReadResponse;

public record MemberRankReadResponse(
        MemberReadResponse memberReadResponse,
        RanksResponse ranksResponse
) {
}
