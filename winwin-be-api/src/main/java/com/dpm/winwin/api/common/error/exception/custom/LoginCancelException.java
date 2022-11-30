package com.dpm.winwin.api.common.error.exception.custom;

import com.dpm.winwin.api.common.error.enums.ErrorMessage;
import lombok.Getter;

@Getter
public class LoginCancelException extends BusinessException {
    public LoginCancelException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
