package com.dpm.winwin.api.post.dto.response;

public record PostMethodResponse(
    String name,
    String message
) {

    public static PostMethodResponse of(String name, String message) {
        return new PostMethodResponse(name, message);
    }
}
