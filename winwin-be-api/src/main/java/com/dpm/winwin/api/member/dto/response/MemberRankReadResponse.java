package com.dpm.winwin.api.member.dto.response;

import com.dpm.winwin.api.post.dto.response.LinkResponse;


import java.util.List;

public record MemberRankReadResponse(
        Long memberId,
        String nickname,
        String image,
        String introduction,
        String rankName,
        String rankImage,
        Integer rankLikeCount,
        List<LinkResponse> profileLinks,
        List<String> givenTalents,
        List<String> takenTalents
) {
}
