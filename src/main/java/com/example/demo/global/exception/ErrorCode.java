package com.example.demo.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    //member
    REQUEST_PARAM_BIND_FAILED(HttpStatus.BAD_REQUEST, "REQ_001", "요청 시 바인딩된 파라미터가 유효하지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_001", "유효하지 않은 인증 요청입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "FBI_001", "유효하지 않은 권한의 요청입니다."),
    NOT_FOUND_ELEMENT(HttpStatus.NOT_FOUND, "NOT_FOUND_001", "일치하는 요소가 잘못됐거나 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "ALLOW_001", "잘못된 http method 방식입니다."),
    DUPLICATE_MEMBER_EXCEPTION(HttpStatus.CONFLICT, "AUTH_002", "멤버 이메일 중복"),
    DUPLICATE_MEMBER_PASSWORD(HttpStatus.CONFLICT, "AUTH_003", "멤버 비밀번호 중복"),
    DUPLICATE_MEMBER_PHONE_NUM(HttpStatus.CONFLICT, "AUTH_004", "멤버 전화번호 중복"),
    WRONG_PASSWORD_INPUT(HttpStatus.CONFLICT, "AUTH_005", "잘못된 비밀번호 형식입니다."),
    WRONG_PHONE_NUM_INPUT(HttpStatus.CONFLICT, "AUTH_006", "잘못된 전화번호 형식입니다."),
    WRONG_EMAIL_INPUT(HttpStatus.CONFLICT, "AUTH_007", "잘못된 이메일 형식입니다."),
    DELETED_MEMBER(HttpStatus.CONFLICT, "Auth_008", "탈퇴한 회원입니다."),

    DUPLICATE_BID_REGISTER_EXCEPTION(HttpStatus.CONFLICT, "BID_001", "입찰 가격을 이미 등록하셨습니다."),
    //likes
    EXIST_LIKES(HttpStatus.CONFLICT, "LIKES_001", "좋아요 내역이 있습니다"),
    //account
    NOT_ENOUGH_ACCOUNT_BALANCE(HttpStatus.FORBIDDEN, "ACCOUNT_001", "계좌 잔액이 충분하지 않습니다."),

    //payment
    NOT_FOUND_PAYMENT_HISTORY(HttpStatus.NOT_FOUND, "PAYMENT_001", "결제 기록이 존재하지 않습니다."),
    NOT_ENOUGH_MEMBER_BALANCE(HttpStatus.FORBIDDEN, "PAYMENT_OO2", "멤버 잔액이 충분하지 않습니다."),

    //common
    UNDER_ZERO_AMOUNT(HttpStatus.FORBIDDEN, "COMMON_001", "0원 이하의 금액은 입금할 수 없습니다."); // account, member에서 각각 계좌 잔액 충전, 멤버 캐시 충전에서 사용


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(final HttpStatus httpStatus, final String code, final String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
