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

    // Record
    CREATE_RECORD_SUCCESS(200, "R001", "기록 등록에 성공하였습니다."),
    GET_MY_TOTAL_RECORD_SUCCESS(200, "R002", "내 기록 누적 수치 조회에 성공하였습니다."),
    GET_MY_RECORD_SUCCESS(200, "R003", "내 기록 수치 조회에 성공하였습니다."),
    GET_ONE_RECORD_SUCCESS(200, "R004", "기록 상세 조회에 성공하였습니다."),
    GET_ALL_RECORD_SUCCESS(200, "R005", "기록 전체 조회에 성공하였습니다."),

    // Challenge
    GET_ONE_CHALLENGE_SUCCESS(200, "C001", "챌린지 상세 조회에 성공하였습니다."),
    GET_ALL_CHALLENGE_SUCCESS(200, "C002", "챌린지 전체 조회에 성공하였습니다."),
    JOIN_CHALLENGE_SUCCESS(200, "C003", "챌린지 가입에 성공하였습니다."),
    JOIN_CHALLENGE_FAIL(200, "C004", "챌린지 가입에 실패하였습니다."),
    CHECK_IN_CHALLENGE_SUCCESS(200, "C005", "챌린지 가입되어 있는 계정입니다."),
    CHECK_IN_CHALLENGE_FAIL(200, "C006", "챌린지 가입되지 않은 계정입니다."),    
    GET_MY_CHALLENGE_SUCCESS(200, "C007", "가입한 챌린지 조회에 성공하였습니다."),


    // Board
    CREATE_BOARD_SUCCESS(200, "B001", "게시글 등록에 성공하였습니다."),
    GET_ONE_BOARD_SUCCESS(200, "B002", "게시글 상세 조회에 성공하였습니다."),
    GET_ALL_BOARD_SUCCESS(200, "B003", "게시글 전체 조회에 성공하였습니다."),
    DELETE_BOARD_SUCCESS(200, "B004", "게시글 삭제에 성공하였습니다."),



    ;

    private final int status;
    private final String code;
    private final String message;
}
