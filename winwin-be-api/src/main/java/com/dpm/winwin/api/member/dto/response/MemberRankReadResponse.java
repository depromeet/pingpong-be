package com.dpm.winwin.api.member.dto.response;

import com.dpm.winwin.domain.repository.member.dto.response.MemberReadResponse;

import java.util.List;

public record MemberRankReadResponse(
        MemberReadResponse memberReadResponse,
        RanksResponse ranksResponse,
        List<String> givenTalents,
        List<String> takenTalents
) {
}
