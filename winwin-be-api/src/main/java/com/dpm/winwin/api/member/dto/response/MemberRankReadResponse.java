package com.dpm.winwin.api.member.dto.response;

import java.util.List;

public record MemberRankReadResponse(
        Long memberId,
        String nickname,
        String image,
        String introduction,
        String rankName,
        String rankImage,
        Integer rankLikeCount,
        String profileLink,
        List<String> givenTalents,
        List<String> takenTalents
) {
}
