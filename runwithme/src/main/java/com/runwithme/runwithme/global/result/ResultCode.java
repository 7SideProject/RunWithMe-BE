package com.runwithme.runwithme.global.result;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ResultCode Convention
 * - 도메인 별로 나누어 관리
 * - [동사_목적어_SUCCESS] 형태로 생성
 * - 코드는 도메인명 앞에서부터 1~2글자로 사용
 * - 메시지는 "~~다."로 마무리
 */

@Getter
@AllArgsConstructor
public enum ResultCode {
    // User
    // 예시
    LOGIN_SUCCESS(200, "U001", "로그인에 성공하였습니다."),
    USER_REQUEST_SUCCESS(200, "U002", "요청을 성공적으로 수행했습니다."),
    INVALID_PARAMETER_FAIL(400, "U101", "잘못된 파라미터입니다."),
    UNAUTHORIZED(401, "U102", "인증에 실패했습니다."),

    // Challenge
    // 예시
    RECORD_SUCCESS(200, "C001", "기록 등록에 성공하였습니다."),



    ;

    private final int status;
    private final String code;
    private final String message;
}
