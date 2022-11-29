package com.dpm.winwin.api.common.error.exception;

import com.dpm.winwin.api.common.error.dto.ErrorResponseDto;
import com.dpm.winwin.api.common.error.exception.custom.BusinessException;
import com.dpm.winwin.api.common.error.exception.custom.InvalidIdTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(InvalidIdTokenException.class)
  public ResponseEntity<ErrorResponseDto> invalidTokenExceptionHandle(BusinessException e) {
    log.warn("invalidTokenException : {}", e);
    return ResponseEntity.badRequest()
            .body(ErrorResponseDto.of(e.getCode(), e.getMessage()));
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponseDto> businessExceptionHandle(BusinessException e) {
    log.warn("businessException : {}", e);
    return ResponseEntity.internalServerError()
        .body(ErrorResponseDto.of(e.getCode(), e.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDto> allUncaughtHandle(Exception e) {
    log.error("allUncaughtHandle : {}", e);
    return ResponseEntity.internalServerError().build();
  }
}
