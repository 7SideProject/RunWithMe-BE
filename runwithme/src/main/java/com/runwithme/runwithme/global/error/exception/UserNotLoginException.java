package com.runwithme.runwithme.global.error.exception;

import com.runwithme.runwithme.global.error.ErrorCode;

public class UserNotLoginException extends BusinessException{

    public UserNotLoginException() {
        super(ErrorCode.AUTHENTICATION_FAIL);
    }
}
