package com.dpm.winwin.api.common.error.exception;

import com.dpm.winwin.api.common.error.dto.ErrorResponseDto;
import com.dpm.winwin.api.common.error.exception.custom.AppleTokenGenerateException;
import com.dpm.winwin.api.common.error.exception.custom.BusinessException;
import com.dpm.winwin.api.common.error.exception.custom.InvalidIdTokenException;
import com.dpm.winwin.api.common.error.exception.custom.LoginCancelException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppleTokenGenerateException.class)
    public ResponseEntity<ErrorResponseDto> appleTokenGenerateExceptionHandle(AppleTokenGenerateException e) {
      log.warn("AppleTokenGenerateException : {}", e);

      return ResponseEntity.badRequest()
              .body(ErrorResponseDto.of(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(LoginCancelException.class)
    public ResponseEntity<ErrorResponseDto> loginCancelExceptionHandle(LoginCancelException e) {
        log.warn("loginCancelException : {}", e);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponseDto.of(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(InvalidIdTokenException.class)
    public ResponseEntity<ErrorResponseDto> invalidTokenExceptionHandle(InvalidIdTokenException e) {
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
