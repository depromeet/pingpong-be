package com.dpm.winwin.api.common.error.enums;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
    INTERVAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, "요청을 처리하는 과정에서 서버가 예상하지 못한 오류가 발생하였습니다."),
    MEMBER_NOT_FOUND(NOT_FOUND, "해당 회원을 찾을 수 없습니다."),
    MAIN_CATEGORY_NOT_FOUND(NOT_FOUND, "해당 대분류 카테고리를 찾을 수 없습니다."),
    MID_CATEGORY_NOT_FOUND(NOT_FOUND, "해당 중분류 카테고리를 찾을 수 없습니다."),
    SUB_CATEGORY_NOT_FOUND(NOT_FOUND, "해당 소분류 카테고리를 찾을 수 없습니다."),
    POST_NOT_FOUND(NOT_FOUND, "해당 포스트를 찾을 수 없습니다."),
    TALENT_NOT_FOUND(NOT_FOUND, "해당 받고 싶은 재능을 찾을 수 없습니다."),
    LINK_NOT_FOUND(NOT_FOUND, "해당 링크를 찾을 수 없습니다."),
    DOES_NOT_MATCH_NONCE(BAD_REQUEST, "ID_TOKEN 값 중 NONCE 값이 일치하지 않습니다."),
    INVALID_ISSUER_VALUE(BAD_REQUEST, "ISSUER 값이 잘못되었습니다."),
    INVALID_CLIENT_ID(BAD_REQUEST, "클라이언트 아이디가 잘못되었습니다."),
    APPLE_TOKEN_GENERATE_FAIL(BAD_REQUEST, "잘못된 액세스 토큰입니다."),
    INVALID_NICKNAME(BAD_REQUEST, "닉네임 입력이 올바르지 않습니다."),
    LOGIN_CANCEL(UNAUTHORIZED, "사용자가 로그인을 취소하였습니다."),
    APPLE_TOKEN_REVOKE_FAIL(BAD_REQUEST, "애플 토큰 삭제에 실패하였습니다."),
    DOES_NOT_MATCH_MEMBER_ID(BAD_REQUEST, "jwt Claim 의 멤버 ID 값과 파라미터 멤버 ID 값이 다릅니다."),
    INVALID_FILE_UPLOAD(BAD_REQUEST, "유효하지 않은 파일 업로드입니다.");

    private final int code;
    private final String phrase;

    ErrorMessage(HttpStatus code, String phrase) {
        this.code = code.value();
        this.phrase = phrase;
    }
}
