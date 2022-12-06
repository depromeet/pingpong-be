package com.dpm.winwin.api.common.error.exception.custom;

import com.dpm.winwin.api.common.error.enums.ErrorMessage;
import lombok.Getter;

@Getter
public class AppleTokenGenerateException extends BusinessException{

    private final String errorDescription;

    public AppleTokenGenerateException(ErrorMessage errorMessage, String errorDescription) {
        super(errorMessage);
        this.errorDescription = errorDescription;
    }
}
