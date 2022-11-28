package com.dpm.winwin.api.member.dto.response;

import com.dpm.winwin.domain.entity.member.Member;

public record MemberCreateResponse(Long memberId,
                                   String nickname) {
    public static MemberCreateResponse from(Member saveMember){
        return new MemberCreateResponse(
                saveMember.getId(),
                saveMember.getNickname()
        );
    }
}
