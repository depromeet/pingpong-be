package com.dpm.winwin.api.common.error.enums;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
  INTERVAL_SERVER_ERROR(INTERNAL_SERVER_ERROR.value(), "요청을 처리하는 과정에서 서버가 예상하지 못한 오류가 발생하였습니다.");

  private final int code;
  private final String phrase;
}
