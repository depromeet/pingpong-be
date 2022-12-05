package com.dpm.winwin.api.member.dto.request;

import com.dpm.winwin.api.post.dto.request.LinkRequest;
import com.dpm.winwin.domain.dto.member.MemberUpdateDto;

import java.util.List;

public record MemberUpdateRequest(
        String nickname,
        String image,
        String introduction,
        String profileLink,
        List<Long> givenTalents,
        List<Long> takenTalents
) {
    public MemberUpdateDto toDto(){
        return new MemberUpdateDto(
                this.nickname,
                this.image,
                this.introduction,
                this.profileLink
        );
    }

}
