package com.dpm.winwin.api.member.dto.response;

import com.dpm.winwin.api.post.dto.response.LinkResponse;
import com.dpm.winwin.domain.repository.member.dto.response.MemberReadResponse;

import java.util.List;

public record MemberRankReadResponse(
        MemberReadResponse memberRead,
        MemberRankResponse memberRank,
        List<LinkResponse> profileLinks,
        List<String> givenTalents,
        List<String> takenTalents
) {
}
