package com.runwithme.runwithme.global.error.exception;

import com.runwithme.runwithme.global.error.ErrorCode;

public class EntityNotFoundException extends BusinessException{

    public EntityNotFoundException(ErrorCode errorCode){
        super(errorCode);
    }
}
