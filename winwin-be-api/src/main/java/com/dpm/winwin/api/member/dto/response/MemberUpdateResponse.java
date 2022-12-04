package com.dpm.winwin.api.member.dto.response;

import com.dpm.winwin.api.post.dto.response.LinkResponse;

import java.util.List;

public record MemberUpdateResponse(
        String nickname,
        String image,
        String introduction,
        List<String> profileLinks,
        List<String> givenTalents,
        List<String> takenTalents
) {
}