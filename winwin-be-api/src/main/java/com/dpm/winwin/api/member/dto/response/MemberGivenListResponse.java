package com.dpm.winwin.api.member.dto.response;

import java.util.List;

public record MemberGivenListResponse(
        List<MemberGivenTalentsResponse> memberGivenTalentsResponseList
) {

    public static MemberGivenListResponse from(List<MemberGivenTalentsResponse> memberGivenTalentsResponseList) {
        return new MemberGivenListResponse(memberGivenTalentsResponseList);
    }

}
