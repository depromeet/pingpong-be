package com.dpm.winwin.api.member.dto.response;


public record MemberGivenTalentsResponse(
        String name
) {

    public static MemberGivenTalentsResponse of(String name) {
        return new MemberGivenTalentsResponse(name);
    }

}
