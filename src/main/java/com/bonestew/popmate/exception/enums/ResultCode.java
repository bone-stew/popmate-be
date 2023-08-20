package com.bonestew.popmate.exception.enums;

import lombok.Getter;

@Getter
public enum ResultCode {

    SUCCESS("성공"),
    FAILURE("실패"),
    BAD_REQUEST("요청에 오류가 있습니다."),
    UNAUTHORIZED("인증이 필요한 요청입니다."),
    FORBIDDEN("권한이 부족합니다."),
    NOT_FOUND("대상을 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED("지원하지 않는 요청입니다."),
    INTERNAL_SERVER_ERROR("서버 오류가 발생했습니다."),
    ;

    private final String message;

    ResultCode(String message) {
        this.message = message;
    }
}
