package com.runwithme.runwithme.global.error;

import com.runwithme.runwithme.global.result.ResultCode;
import com.runwithme.runwithme.global.result.ResultResponseDto;
import com.runwithme.runwithme.global.webhook.NotificationManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : getFieldErrors(e)) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity
                .status(ResultCode.INVALID_PARAMETER_FAIL.getStatus())
                .body(ResultResponseDto.of(ResultCode.INVALID_PARAMETER_FAIL, errors));
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
}
