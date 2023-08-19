package com.example.demo.global.exception.error.member;

import com.example.demo.global.exception.ErrorCode;

public class DuplicateMemberPasswordException extends RuntimeException{
    public DuplicateMemberPasswordException() {
        super(ErrorCode.DUPLICATE_MEMBER_PASSWORD.getMessage());
    }
    public DuplicateMemberPasswordException(String message){
        super(message);
    }
    public DuplicateMemberPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
    public DuplicateMemberPasswordException(Throwable cause){
        super(cause);
    }
}
