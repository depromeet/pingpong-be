package com.dpm.winwin.domain.dto.talent;

import com.dpm.winwin.domain.entity.category.SubCategory;
import com.dpm.winwin.domain.entity.talent.MemberTalent;
import com.dpm.winwin.domain.entity.member.enums.TalentType;

public record MemberTalentDto(
        Long talentId,
        SubCategory talent,
        TalentType type
) {
    public static MemberTalent toEntity(MemberTalentDto memberTalentDto){
        return MemberTalent.of(memberTalentDto.talent(), memberTalentDto.type());
    }
}
