package com.dpm.winwin.chatting.common.error.exception.custom;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

  private final int code;

  public BusinessException(int code, String phrase) {
    super(phrase);
    this.code = code;
  }
}
