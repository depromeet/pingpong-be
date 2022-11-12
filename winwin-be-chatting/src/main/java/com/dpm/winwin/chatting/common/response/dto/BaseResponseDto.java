package com.dpm.winwin.chatting.common.response.dto;

public record BaseResponseDto<T>(String message, T data) {

    public static <T> BaseResponseDto<T> ok(T data){
        return new BaseResponseDto<>("success",data);
    }
}
