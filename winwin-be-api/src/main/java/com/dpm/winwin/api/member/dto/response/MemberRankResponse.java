package com.dpm.winwin.api.member.dto.response;

public record MemberRankResponse(
        String name,
        String image,
        Integer likes
) {
    public static MemberRankResponse of(String name, String image, Integer likes) {
        return new MemberRankResponse(name,
                image,
                likes);
    }
}
