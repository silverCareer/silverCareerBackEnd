package com.example.demo.global.exception.error.s3;

import com.example.demo.global.exception.ErrorCode;

public class IllegalArgumentException extends java.lang.IllegalArgumentException {
    public IllegalArgumentException() {
        super(ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION.getMessage());
    }

    public IllegalArgumentException(String message) {
        super(message);
    }

    public IllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalArgumentException(Throwable cause) {
        super(cause);
    }

}
