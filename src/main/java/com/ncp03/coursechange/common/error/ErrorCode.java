package com.ncp03.coursechange.common.error;

import org.springframework.http.HttpStatus;

/** API 에러 코드. 프론트는 code 값으로 분기하고, message는 화면에 그대로 보여 줘도 된다. */
public enum ErrorCode {
    MISSING_STUDENT_ID(HttpStatus.UNAUTHORIZED, "X-Student-Id 헤더가 필요합니다."),
    INVALID_STUDENT_ID(HttpStatus.BAD_REQUEST, "X-Student-Id 헤더는 1 이상의 숫자여야 합니다."),
    STUDENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 학생입니다."),
    COURSE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 강의(분반)입니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 주소를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드입니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");

    private final HttpStatus status;
    private final String defaultMessage;

    ErrorCode(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }

    public HttpStatus getStatus() { return status; }
    public String getDefaultMessage() { return defaultMessage; }
}
