package com.dpm.winwin.api.member.dto.request;

import com.dpm.winwin.domain.dto.member.MemberUpdateDto;

import java.util.List;

public record MemberUpdateRequest(
        String nickname,
        String introduction,
        String profileLink,
        List<Long> givenTalents,
        List<Long> takenTalents
) {
    public MemberUpdateDto toDto(){
        return new MemberUpdateDto(
                this.nickname,
                this.introduction,
                this.profileLink
        );
    }

}
