package com.dpm.winwin.domain.repository.member.dto.response;

import com.querydsl.core.annotations.QueryProjection;

public record MemberReadResponse(Long memberId, String nickname, String image,
                                 String introductions, int exchangeCount, String profileLink,
                                 String givenTalent, String takenTalent) {
    @QueryProjection
    public MemberReadResponse{
    }

}
