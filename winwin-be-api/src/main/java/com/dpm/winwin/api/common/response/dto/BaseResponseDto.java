package com.dpm.winwin.api.common.response.dto;

public record BaseResponseDto<T>(String message, T data) {

    public static <T> BaseResponseDto<T> ok(T data){
        return new BaseResponseDto<>("success",data);
    }
    public static <T> BaseResponseDto<T> error(T data){
        return new BaseResponseDto<>("error",data);
    }
}
