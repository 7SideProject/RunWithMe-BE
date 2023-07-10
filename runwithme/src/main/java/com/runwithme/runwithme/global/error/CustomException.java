package com.runwithme.runwithme.global.error;

import com.runwithme.runwithme.global.result.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private final ResultCode resultCode;
}
