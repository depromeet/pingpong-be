package com.dpm.winwin.api.common.error.exception.custom;

import com.dpm.winwin.api.common.error.enums.ErrorMessage;

public class LoginCancelException extends BusinessException {
    public LoginCancelException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
