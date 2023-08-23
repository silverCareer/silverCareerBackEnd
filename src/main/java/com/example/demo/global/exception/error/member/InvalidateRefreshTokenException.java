package com.example.demo.global.exception.error.member;

import com.example.demo.global.exception.ErrorCode;

public class InvalidateRefreshTokenException extends RuntimeException {
    public InvalidateRefreshTokenException() {
        super(ErrorCode.INVALIDATE_REFRESH_TOKEN.getMessage());
    }

    public InvalidateRefreshTokenException(String message) {
        super(message);
    }

    public InvalidateRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidateRefreshTokenException(Throwable cause) {
        super(cause);
    }
}
