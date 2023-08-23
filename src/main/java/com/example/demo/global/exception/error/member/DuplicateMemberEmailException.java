package com.example.demo.global.exception.error.member;

import com.example.demo.global.exception.ErrorCode;

public class DuplicateMemberEmailException extends RuntimeException{
    public DuplicateMemberEmailException() {
        super(ErrorCode.DUPLICATE_MEMBER_EMAIL.getMessage());
    }
    public DuplicateMemberEmailException(String message){
        super(message);
    }
    public DuplicateMemberEmailException(String message, Throwable cause) {
        super(message, cause);
    }
    public DuplicateMemberEmailException(Throwable cause){
        super(cause);
    }
}

