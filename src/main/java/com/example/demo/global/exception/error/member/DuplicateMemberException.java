package com.example.demo.global.exception.error.member;

import com.example.demo.global.exception.ErrorCode;

public class DuplicateMemberException extends RuntimeException {
    public DuplicateMemberException() {
        super(ErrorCode.DUPLICATE_MEMBER_EXCEPTION.getMessage());
    }
    public DuplicateMemberException(String message){
        super(message);
    }
    public DuplicateMemberException(String message, Throwable cause) {
        super(message, cause);
    }
    public DuplicateMemberException(Throwable cause){
        super(cause);
    }
}
