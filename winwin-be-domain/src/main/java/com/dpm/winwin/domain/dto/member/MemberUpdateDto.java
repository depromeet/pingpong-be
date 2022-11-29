package com.dpm.winwin.domain.dto.member;

public record MemberUpdateDto(
        String nickname,
        String image,
        String introductions,
        String profileLink
) {
}
