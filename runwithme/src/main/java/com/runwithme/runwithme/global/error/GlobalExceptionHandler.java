package com.runwithme.runwithme.global.error;

import com.runwithme.runwithme.global.error.exception.BusinessException;
import com.runwithme.runwithme.global.result.ResultCode;
import com.runwithme.runwithme.global.result.ResultResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    protected ResponseEntity<ResultResponseDto> badParameter(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ResultResponseDto.of(ResultCode.INVALID_PARAMETER_FAIL, e.getMessage())
        );
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorResponseDto> handleBusinessException(BusinessException e) {
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponseDto response = ErrorResponseDto.of(errorCode);
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }
}
