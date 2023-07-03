package com.runwithme.runwithme.global.error;

import com.runwithme.runwithme.global.error.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ErrorCode Convention
 * - 도메인 별로 나누어 관리
 * - [주체_이유] 형태로 생성
 * - 코드는 도메인명 앞에서부터 1~2글자로 사용
 * - 메시지는 "~~다."로 마무리
 */

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // User
    USER_NOT_FOUND(400, "U001", "존재하지 않는 유저입니다."),

    // Challenge
    CHALLENGE_NOT_FOUND(400, "C001", "존재하지 않는 챌린지입니다."),
    CHALLENGE_JOIN_ALREADY_EXIST(400, "C002", "이미 가입한 챌린지 입니다."),

    // Record
    RECORD_ALREADY_EXIST(400, "R001", "오늘 이미 기록을 등록 하였습니다."),

    ;

    private final int status;
    private final String code;
    private final String message;


}
