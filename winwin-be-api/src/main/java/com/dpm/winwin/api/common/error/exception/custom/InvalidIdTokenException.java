package com.dpm.winwin.api.common.error.exception.custom;

import com.dpm.winwin.api.common.error.enums.ErrorMessage;
import lombok.Getter;

@Getter
public class InvalidIdTokenException extends BusinessException {

    public InvalidIdTokenException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
