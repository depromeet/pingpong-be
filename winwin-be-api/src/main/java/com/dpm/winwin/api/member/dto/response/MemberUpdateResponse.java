package com.dpm.winwin.api.member.dto.response;

import java.util.List;

public record MemberUpdateResponse(
        String nickname,
        String image,
        String introduction,
        String profileLink,
        List<TalentResponse> givenTalents,
        List<TalentResponse> takenTalents
) {
}
