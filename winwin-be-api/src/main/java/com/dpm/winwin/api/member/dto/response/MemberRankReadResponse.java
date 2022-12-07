package com.dpm.winwin.api.member.dto.response;

import java.util.List;

public record MemberRankReadResponse(
        Long memberId,
        String nickname,
        String image,
        String introduction,
        String ranks,
        String ranksImage,
        Integer likeCount,
        String profileLink,
        List<String> givenTalents,
        List<String> takenTalents
) {
}
