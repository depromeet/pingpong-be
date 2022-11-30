package com.dpm.winwin.api.common.error.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
    INTERVAL_SERVER_ERROR(INTERNAL_SERVER_ERROR.value(), "요청을 처리하는 과정에서 서버가 예상하지 못한 오류가 발생하였습니다."),
    MEMBER_NOT_FOUND(NOT_FOUND.value(), "해당 회원을 찾을 수 없습니다."),
    MAIN_CATEGORY_NOT_FOUND(NOT_FOUND.value(), "해당 대분류 카테고리를 찾을 수 없습니다."),
    MID_CATEGORY_NOT_FOUND(NOT_FOUND.value(), "해당 중분류 카테고리를 찾을 수 없습니다."),
    SUB_CATEGORY_NOT_FOUND(NOT_FOUND.value(), "해당 소분류 카테고리를 찾을 수 없습니다."),
    POST_NOT_FOUND(NOT_FOUND.value(), "해당 포스트를 찾을 수 없습니다."),
    TALENT_NOT_FOUND(NOT_FOUND.value(), "해당 받고 싶은 재능을 찾을 수 없습니다."),
    LINK_NOT_FOUND(NOT_FOUND.value(), "해당 링크를 찾을 수 없습니다."),
    DOES_NOT_MATCH_NONCE(BAD_REQUEST.value(), "ID_TOKEN 값 중 NONCE 값이 일치하지 않습니다."),
    INVALID_ISSUER_VALUE(BAD_REQUEST.value(), "ISSUER 값이 잘못되었습니다."),
    INVALID_CLIENT_ID(BAD_REQUEST.value(), "클라이언트 아이디가 잘못되었습니다."),
    APPLE_TOKEN_GENERATE_FAIL(BAD_REQUEST.value(), "잘못된 액세스 토큰입니다.");

    private final int code;
    private final String phrase;
    }
