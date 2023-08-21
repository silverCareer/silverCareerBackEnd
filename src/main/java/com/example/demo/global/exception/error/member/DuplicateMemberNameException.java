package com.example.demo.global.exception.error.member;

import com.example.demo.global.exception.ErrorCode;

public class DuplicateMemberNameException extends RuntimeException{
    public DuplicateMemberNameException() {
        super(ErrorCode.DUPLICATE_MEMBER_NAME.getMessage());
    }
    public DuplicateMemberNameException(String message){
        super(message);
    }
    public DuplicateMemberNameException(String message, Throwable cause) {
        super(message, cause);
    }
    public DuplicateMemberNameException(Throwable cause){
        super(cause);
    }
}
