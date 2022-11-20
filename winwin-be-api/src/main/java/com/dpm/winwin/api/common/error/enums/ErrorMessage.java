package com.dpm.winwin.api.common.error.enums;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
  INTERVAL_SERVER_ERROR(INTERNAL_SERVER_ERROR.value(), "요청을 처리하는 과정에서 서버가 예상하지 못한 오류가 발생하였습니다."),
  MEMBER_NOT_FOUND(NOT_FOUND.value(), "해당 회원을 찾을 수 없습니다."),
  MAIN_CATEGORY_NOT_FOUND(NOT_FOUND.value(), "해당 대분류 카테고리를 찾을 수 없습니다."),
  MID_CATEGORY_NOT_FOUND(NOT_FOUND.value(), "해당 중분류 카테고리를 찾을 수 없습니다."),
  SUB_CATEGORY_NOT_FOUND(NOT_FOUND.value(), "해당 소분류 카테고리를 찾을 수 없습니다."),
  POST_NOT_FOUND(NOT_FOUND.value(), "해당 포스트를 찾을 수 없습니다."),
  TALENT_NOT_FOUND(NOT_FOUND.value(), "해당 받고 싶은 재능을 찾을 수 없습니다.");

  private final int code;
  private final String phrase;
}
