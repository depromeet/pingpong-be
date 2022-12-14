package com.dpm.winwin.domain.entity.member.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum RoleType {

    USER("ROLE_USER", "일반 사용자 권한"),
    ADMIN("ROLE_ADMIN", "관리자 권한"),
    GUEST("GUEST", "게스트 권한");

    private final String code;

    private final String displayName;

    RoleType(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public static RoleType of(String code) {
        return Arrays.stream(RoleType.values())
            .filter(role -> role.getCode().equals(code))
            .findAny()
            .orElse(GUEST);
    }
}
