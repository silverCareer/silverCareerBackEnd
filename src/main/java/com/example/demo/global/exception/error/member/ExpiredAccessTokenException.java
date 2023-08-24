package com.example.demo.global.exception.error.member;

import com.example.demo.global.exception.ErrorCode;

public class ExpiredAccessTokenException extends RuntimeException {
    public ExpiredAccessTokenException() {
        super(ErrorCode.EXPIRED_ACCESS_TOKEN.getMessage());
    }

    public ExpiredAccessTokenException(String message) {
        super(message);
    }

    public ExpiredAccessTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpiredAccessTokenException(Throwable cause) {
        super(cause);
    }
}
