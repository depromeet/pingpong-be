package com.dpm.winwin.domain.dto.member;

import com.dpm.winwin.domain.dto.link.LinkDto;

import java.util.List;

public record MemberUpdateDto(
        String nickname,
        String image,
        String introduction,
        List<LinkDto> profileLinks
) {
}
