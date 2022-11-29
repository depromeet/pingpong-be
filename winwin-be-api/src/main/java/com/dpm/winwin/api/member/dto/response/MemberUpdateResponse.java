package com.dpm.winwin.api.member.dto.response;

import java.util.List;

public record MemberUpdateResponse(
        Long id,
        String nickname,
        String image,
        String introductions,
        String profileLink,
        List<String> givenTalents,
        List<String> takenTalents
) {
}
