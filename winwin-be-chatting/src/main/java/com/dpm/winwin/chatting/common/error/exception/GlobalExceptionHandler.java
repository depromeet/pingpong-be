package com.dpm.winwin.chatting.common.error.exception;

import com.dpm.winwin.chatting.common.error.dto.ErrorResponseDto;
import com.dpm.winwin.chatting.common.error.exception.custom.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponseDto> businessExceptionHandle(BusinessException e) {
    log.error("businessException : {}", e);
    return ResponseEntity.internalServerError()
        .body(ErrorResponseDto.of(e.getCode(), e.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDto> allUncaughtHandle(Exception e) {
    log.error("allUncaughtHandle : {}", e);
    return ResponseEntity.internalServerError().build();
  }
}
