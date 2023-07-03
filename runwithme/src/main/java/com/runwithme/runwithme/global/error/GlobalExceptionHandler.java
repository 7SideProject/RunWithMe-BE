package com.runwithme.runwithme.global.error;

import com.runwithme.runwithme.global.result.ResultCode;
import com.runwithme.runwithme.global.result.ResultResponseDto;
import com.runwithme.runwithme.global.webhook.NotificationManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Enumeration;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final NotificationManager notificationManager;


    @ExceptionHandler({ErrorCodeException.class})
    protected ResponseEntity<ErrorResponse> exceptionHandler(CustomException e, HttpServletRequest request) {
//        notificationManager.sendNotification(errorCode, request.getRequestURI(), getParams(request));
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    @Getter
    @Builder
    public static class ErrorResponse {
        private final LocalDateTime timestamp = LocalDateTime.now();
        private final int status;
        private final String code;
        private final String message;

        public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
            return ResponseEntity
                    .status(errorCode.getStatus())
                    .body(ErrorResponse.builder()
                            .status(errorCode.getStatus())
                            .code(errorCode.getCode())
                            .message(errorCode.getMessage())
                            .build()
                    );
        }
    }

    /*@ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    protected ResponseEntity<ResultResponseDto> badParameter(RuntimeException e, HttpServletRequest request) {
        notificationManager.sendNotification(e, request.getRequestURI(), getParams(request));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ResultResponseDto.of(ResultCode.INVALID_PARAMETER_FAIL, e.getMessage())
        );
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ResultResponseDto> exception(Exception e, HttpServletRequest request) {
        notificationManager.sendNotification(e, request.getRequestURI(), getParams(request));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ResultResponseDto.of(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage())
        );
    }*/

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

    @Getter
    public static class ErrorCodeException extends Exception {
        private final ErrorCode error;

        public ErrorCodeException(ErrorCode e) {
            super(e.getMessage());
            this.error = e;
        }

    }

    /*@RestControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(Exception.class)
        public ResponseEntity<?> handleException(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }*/
}
