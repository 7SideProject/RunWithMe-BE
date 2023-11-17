package com.runwithme.runwithme.global.error;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.runwithme.runwithme.global.result.ResultCode;
import com.runwithme.runwithme.global.result.ResultResponseDto;
import com.runwithme.runwithme.global.webhook.NotificationManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private final NotificationManager notificationManager;

	@ExceptionHandler({CommonException.class})
	protected ResponseEntity<ResultResponseDto> exceptionHandler(CommonException e, HttpServletRequest request) {
		notificationManager.sendNotification(e, request.getRequestURI(), getParams(request));
		ResultCode resultCode = e.getResultCode();
		return ResponseEntity
			.status(resultCode.getStatus())
			.body(ResultResponseDto.of(resultCode, resultCode.getMessage()));
	}

	@ExceptionHandler({ConstraintViolationException.class})
	protected ResponseEntity<ResultResponseDto> validExceptionHandler(ConstraintViolationException e) {
		return ResponseEntity
			.status(ResultCode.CHALLENGE_PARAMETER_FAIL.getStatus())
			.body(ResultResponseDto.of(ResultCode.CHALLENGE_PARAMETER_FAIL, getResultMessage(e.getConstraintViolations().iterator())));
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		Map<String, String> errors = getFieldErrors(e).stream()
			.collect(Collectors.toMap(FieldError::getField, err -> err.getDefaultMessage() == null ? "null" : err.getDefaultMessage()));

		return ResponseEntity
			.status(ResultCode.INVALID_PARAMETER_FAIL.getStatus())
			.body(ResultResponseDto.of(ResultCode.INVALID_PARAMETER_FAIL, errors));
	}

	@ExceptionHandler({Exception.class})
	protected ResponseEntity<ResultResponseDto> exceptionHandler(Exception e, HttpServletRequest request) {
		notificationManager.sendNotification(e, request.getRequestURI(), getParams(request));
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(ResultResponseDto.of(ResultCode.INTERNAL_SERVER_ERROR));
	}

	private static List<FieldError> getFieldErrors(MethodArgumentNotValidException e) {
		return e.getBindingResult().getFieldErrors();
	}

	private String getParams(HttpServletRequest req) {
		StringBuilder params = new StringBuilder();
		Enumeration<String> keys = req.getParameterNames();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			params.append(
				"""
					- %s : %s
					""".formatted(key, req.getParameter(key))
			);
		}
		return params.toString();
	}

	protected Map<String, StringBuilder> getResultMessage(final Iterator<ConstraintViolation<?>> violationIterator) {
		final Map<String, StringBuilder> resultMessage = new HashMap<>();
		while (violationIterator.hasNext() == true) {
			final ConstraintViolation<?> constraintViolation = violationIterator.next();
			final StringBuilder messageBuilder = new StringBuilder();
			messageBuilder
				.append(constraintViolation.getMessage())
				.append(" ( 잘못된 값 : ")
				.append(constraintViolation.getInvalidValue())
				.append(" )");

			resultMessage.put(getPropertyName(constraintViolation.getPropertyPath().toString()), messageBuilder);
		}

		return resultMessage;
	}

	protected String getPropertyName(String propertyPath) {
		return propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
	}
}
