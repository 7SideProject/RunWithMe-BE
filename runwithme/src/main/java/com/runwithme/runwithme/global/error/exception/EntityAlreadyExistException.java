package com.runwithme.runwithme.global.error.exception;

import com.runwithme.runwithme.global.error.ErrorCode;

public class EntityAlreadyExistException extends BusinessException {

    public EntityAlreadyExistException(ErrorCode errorCode){
        super(errorCode);
    }
}
