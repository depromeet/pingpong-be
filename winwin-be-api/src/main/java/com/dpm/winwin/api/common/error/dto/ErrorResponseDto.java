package com.dpm.winwin.api.common.error.dto;

public record ErrorResponseDto(int code, String phrase) {
    public static ErrorResponseDto of(int code,String phrase){
        return new ErrorResponseDto(code, phrase);
    }
}
