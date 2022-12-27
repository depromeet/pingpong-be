package com.dpm.winwin.api.member.dto.response;

import com.dpm.winwin.domain.entity.member.MemberTalent;

public record TalentResponse(
    Long id,
    String content
) {

    public static TalentResponse of(MemberTalent memberTalent) {
        return new TalentResponse(
            memberTalent.getId(),
            memberTalent.getTalent().getName()
        );
    }
}
