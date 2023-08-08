package com.example.demo.global.exception.error;

import com.example.demo.global.exception.ErrorCode;

public class DuplicatedMemberException extends RuntimeException {
    public DuplicatedMemberException() {
        super();
    }

    public DuplicatedMemberException(String message, Throwable exp) {
        super(message, exp);
    }

    public DuplicatedMemberException(String message) {
        super(message);
    }

    public DuplicatedMemberException(Throwable exp) {
        super(exp);
    }
}
