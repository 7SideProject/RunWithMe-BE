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
    LOGIN_SUCCESS(200, "U001", "로그인에 성공하였습니다."),
    USER_REQUEST_SUCCESS(200, "U002", "요청을 성공적으로 수행했습니다."),
    INVALID_PARAMETER_FAIL(400, "U101", "잘못된 파라미터입니다."),
    USER_NOT_FOUND(400, "U001", "존재하지 않는 유저입니다."),
    SEQ_NOT_FOUND(400, "UXXX", "해당 SEQ를 가진 유저가 존재하지 않습니다."),
    EMAIL_EXISTS(400, "UXXX", "이미 존재하는 이메일입니다."),
    UNAUTHORIZED(401, "U102", "인증에 실패했습니다."),
    INTERNAL_SERVER_ERROR(500, "U103", "서버 오류입니다."),
    DELETED_USER(400, "UXXX", "삭제된 회원입니다."),
    BAD_PASSWORD(400, "UXXX", "잘못된 패스워드입니다."),

    // Record
    CREATE_RECORD_SUCCESS(200, "R001", "기록 등록에 성공하였습니다."),
    GET_MY_TOTAL_RECORD_SUCCESS(200, "R002", "내 기록 누적 수치 조회에 성공하였습니다."),
    GET_MY_RECORD_SUCCESS(200, "R003", "내 기록 수치 조회에 성공하였습니다."),
    GET_ONE_RECORD_SUCCESS(200, "R004", "기록 상세 조회에 성공하였습니다."),
    GET_ALL_RECORD_SUCCESS(200, "R005", "기록 전체 조회에 성공하였습니다."),
    ADD_COORDINATES_SUCCESS(200, "R006", "좌표 등록에 성공하였습니다."),
    ADD_COORDINATES_FAIL(200, "R007", "좌표 등록에 실패하였습니다."),
    RECORD_ALREADY_EXIST(400, "R001", "오늘 이미 기록을 등록 하였습니다."),

    // Challenge
    GET_ONE_CHALLENGE_SUCCESS(200, "C001", "챌린지 상세 조회에 성공하였습니다."),
    GET_ALL_CHALLENGE_SUCCESS(200, "C002", "챌린지 전체 조회에 성공하였습니다."),
    JOIN_CHALLENGE_SUCCESS(200, "C003", "챌린지 가입에 성공하였습니다."),
    JOIN_CHALLENGE_FAIL(200, "C004", "챌린지 가입에 실패하였습니다."),
    CHECK_IN_CHALLENGE_SUCCESS(200, "C005", "챌린지 가입되어 있는 계정입니다."),
    CHECK_IN_CHALLENGE_FAIL(200, "C006", "챌린지 가입되지 않은 계정입니다."),    
    GET_MY_CHALLENGE_SUCCESS(200, "C007", "가입한 챌린지 조회에 성공하였습니다."),
    CHALLENGE_NOT_FOUND(400, "C001", "존재하지 않는 챌린지입니다."),
    CHALLENGE_JOIN_ALREADY_EXIST(400, "C002", "이미 가입한 챌린지 입니다."),

    // Board
    CREATE_BOARD_SUCCESS(200, "B001", "게시글 등록에 성공하였습니다."),
    GET_ONE_BOARD_SUCCESS(200, "B002", "게시글 상세 조회에 성공하였습니다."),
    GET_ALL_BOARD_SUCCESS(200, "B003", "게시글 전체 조회에 성공하였습니다."),
    DELETE_BOARD_SUCCESS(200, "B004", "게시글 삭제에 성공하였습니다."),

    // Image
    IMAGE_NOT_FOUND(400, "IXXX", "이미지를 찾을 수 없습니다."),
    FAILED_CONVERT(400, "IXXX", "잘못된 파일입니다."),

    // Auth
    FAILED_GENERATE_TOKEN(400, "TXXX", "토큰을 생성할 수 없습니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
