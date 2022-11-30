package com.dpm.winwin.chatting.common.error.exception.custom;

import com.dpm.winwin.chatting.common.error.enums.ErrorMessage;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

  private final int code;

  public BusinessException(ErrorMessage errorMessage) {
    super(errorMessage.getPhrase());
    this.code = errorMessage.getCode();
  }
}
