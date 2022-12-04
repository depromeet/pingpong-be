package com.dpm.winwin.api.common.error.exception.custom;

import com.dpm.winwin.api.common.error.enums.ErrorMessage;

public class InvalidIdTokenException extends BusinessException {

    public InvalidIdTokenException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
