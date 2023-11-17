package com.runwithme.runwithme.global.error.jwt;

import com.runwithme.runwithme.global.error.CustomException;
import com.runwithme.runwithme.global.result.ResultCode;

public class JwtException extends CustomException {
	public JwtException(ResultCode resultCode) {
		super(resultCode);
	}
}
