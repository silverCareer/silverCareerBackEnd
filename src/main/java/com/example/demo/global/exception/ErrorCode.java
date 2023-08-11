package com.example.demo.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    REQUEST_PARAM_BIND_FAILED(HttpStatus.BAD_REQUEST, "REQ_001", "요청 시 바인딩된 파라미터가 유효하지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_001", "유효하지 않은 인증 요청입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "FBI_001", "유효하지 않은 권한의 요청입니다."),
    NOT_FOUND_ELEMENT(HttpStatus.NOT_FOUND, "NOT_FOUND_001", "일치하는 요소가 잘못됐거나 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "ALLOW_001", "잘못된 http method 방식입니다."),

    DUPLICATE_MEMBER_EXCEPTION(HttpStatus.CONFLICT, "AUTH_002", "멤버 이메일 중복"),
    DUPLICATE_BID_REGISTER_EXCEPTION(HttpStatus.CONFLICT, "BID_001", "입찰 가격을 이미 등록하셨습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(final HttpStatus httpStatus, final String code, final String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
