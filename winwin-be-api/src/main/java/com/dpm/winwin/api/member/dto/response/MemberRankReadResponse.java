package com.dpm.winwin.api.member.dto.response;

import com.dpm.winwin.domain.repository.member.dto.response.MemberReadResponse;

import java.util.List;

public record MemberRankReadResponse(
        MemberReadResponse memberReadResponse,
        MemberRankResponse memberRankResponse,
        List<String> givenTalents,
        List<String> takenTalents
) {
}
