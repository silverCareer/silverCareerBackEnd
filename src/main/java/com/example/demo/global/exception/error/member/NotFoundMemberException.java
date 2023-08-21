package com.example.demo.global.exception.error.member;

import com.example.demo.global.exception.ErrorCode;

public class NotFoundMemberException  extends RuntimeException{
    public NotFoundMemberException() {
        super(ErrorCode.NOT_EXISTED_MEMBER_EXCEPTION.getMessage());
    }
    public NotFoundMemberException(String message){
        super(message);
    }
    public NotFoundMemberException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotFoundMemberException(Throwable cause){
        super(cause);
    }
}
