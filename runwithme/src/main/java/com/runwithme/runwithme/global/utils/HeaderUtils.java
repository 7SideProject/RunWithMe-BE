package com.runwithme.runwithme.global.utils;

import org.apache.commons.codec.CharEncoding;
import org.springframework.http.MediaType;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HeaderUtils {
	private final static String HEADER_AUTHORIZATION = "Authorization";
	private final static String TOKEN_PREFIX = "Bearer ";

	public static void setAccessToken(HttpServletResponse response, String value) {
		response.addHeader(HEADER_AUTHORIZATION, TOKEN_PREFIX + value);
		response.setCharacterEncoding(CharEncoding.UTF_8);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	}

	public static String getAccessToken(HttpServletRequest request) {
		String headerValue = request.getHeader(HEADER_AUTHORIZATION);

		if (headerValue == null) {
			return null;
		}

		if (headerValue.startsWith(TOKEN_PREFIX)) {
			return headerValue.substring(TOKEN_PREFIX.length());
		}

		return null;
	}
}
