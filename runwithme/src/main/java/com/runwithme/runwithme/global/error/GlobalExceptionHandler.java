package com.runwithme.runwithme.global.error;

import com.runwithme.runwithme.global.result.ResultCode;
import com.runwithme.runwithme.global.result.ResultResponseDto;
import com.runwithme.runwithme.global.webhook.NotificationManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Enumeration;
import java.util.Iterator;

import static com.runwithme.runwithme.global.result.ResultCode.VALID_PARAMETER_FAIL;

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
                .status(HttpStatus.BAD_REQUEST)
                .body(ResultResponseDto.of(VALID_PARAMETER_FAIL, getResultMessage(e.getConstraintViolations().iterator())));
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

    protected String getResultMessage(final Iterator<ConstraintViolation<?>> violationIterator) {
        final StringBuilder resultMessageBuilder = new StringBuilder();
        while (violationIterator.hasNext() == true) {
            final ConstraintViolation<?> constraintViolation = violationIterator.next();
            resultMessageBuilder
                    .append(getPropertyName(constraintViolation.getPropertyPath().toString())) // 유효성 검사가 실패한 속성
                    .append("의 ")
                    .append(constraintViolation.getMessage()) // 유효성 검사 실패 시 메시지
                    .append(" 잘못 들어온 값 : ")
                    .append(constraintViolation.getInvalidValue()); // 유효하지 않은 값

            if (violationIterator.hasNext() == true) {
                resultMessageBuilder.append(", ");
            }
        }

        return resultMessageBuilder.toString();
    }

    protected String getPropertyName(String propertyPath) {
        return propertyPath.substring(propertyPath.lastIndexOf('.') + 1); // 전체 속성 경로에서 속성 이름만 가져온다.
    }
}
