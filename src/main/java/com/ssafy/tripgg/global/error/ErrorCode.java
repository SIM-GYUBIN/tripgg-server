package com.ssafy.tripgg.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(400, "C001", "잘못된 입력값입니다"),
    RESOURCE_NOT_FOUND(404, "C002", "요청한 리소스를 찾을 수 없습니다"),
    INTERNAL_SERVER_ERROR(500, "C003", "서버 내부 오류가 발생했습니다"),

    // User
    USER_NOT_FOUND(404, "U001", "사용자를 찾을 수 없습니다"),
    INVALID_AUTH_TOKEN(401, "U002", "유효하지 않은 인증 토큰입니다"),

    // Course
    COURSE_NOT_FOUND(404, "CO001", "코스를 찾을 수 없습니다"),

    // Course Progress
    COURSE_PROCESS_NOT_FOUND(404, "CO002", "코스 진행을 찾을 수 없습니다"),
    COURSE_ALREADY_IN_PROGRESS(404, "CO003", "이미 도전 중인 코스입니다"),
    COURSE_ALREADY_COMPLETED(404, "CO004", "이미 완료한 코스입니다"),
    COURSE_ALREADY_ABANDONED(404, "CO005", "이미 포기한 코스입니다"),
    COURSE_NOT_IN_PROGRESS(404, "CO006", "코스 도전 상태가 아닙니다"),

    // Verification
    INVALID_LOCATION(400, "V001", "유효하지 않은 위치입니다"),
    VERIFICATION_FAILED(400, "V002", "인증에 실패했습니다");

    private final int status;
    private final String code;
    private final String message;
}