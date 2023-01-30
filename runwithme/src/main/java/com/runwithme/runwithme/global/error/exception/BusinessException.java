package com.runwithme.runwithme.global.error.exception;

import com.runwithme.runwithme.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
