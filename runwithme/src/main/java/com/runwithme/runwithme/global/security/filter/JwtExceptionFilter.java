package com.runwithme.runwithme.global.security.filter;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runwithme.runwithme.global.error.jwt.JwtException;
import com.runwithme.runwithme.global.result.ResultCode;
import com.runwithme.runwithme.global.result.ResultResponseDto;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtExceptionFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		try {
			chain.doFilter(request, response);
		} catch (JwtException e) {
			setErrorResponse(response, e);
		}
	}

	private void setErrorResponse(HttpServletResponse response, JwtException e) {
		final ResultCode errorCode = e.getResultCode();
		final ResultResponseDto errorResponse = ResultResponseDto.of(errorCode);
		try {
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setStatus(HttpStatus.UNAUTHORIZED.value());

			final ObjectMapper om = new ObjectMapper();
			om.writeValue(response.getOutputStream(), errorResponse);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
