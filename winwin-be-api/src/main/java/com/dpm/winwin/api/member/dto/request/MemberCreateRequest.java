package com.dpm.winwin.api.member.dto.request;

import com.dpm.winwin.domain.entity.member.Member;

public record MemberCreateRequest(String nickname
) {
    public Member toEntity(){
        return Member.builder()
                .nickname(this.nickname)
                .build();
    }
}
