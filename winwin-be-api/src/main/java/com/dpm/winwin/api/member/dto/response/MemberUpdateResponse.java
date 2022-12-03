package com.dpm.winwin.api.member.dto.response;

import java.util.List;

public record MemberUpdateResponse(
        String nickname,
        String image,
        String introduction,
        List<com.dpm.winwin.api.post.dto.response.LinkResponse> profileLinks,
        List<String> givenTalents,
        List<String> takenTalents
) {
}
