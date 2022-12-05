package com.dpm.winwin.api.member.dto.response;

public record RanksResponse(
    String name,
    String image,
    String condition
) {

    public static RanksResponse of(String name, String image, String condition) {
        return new RanksResponse(name, image, condition);
    }

}
