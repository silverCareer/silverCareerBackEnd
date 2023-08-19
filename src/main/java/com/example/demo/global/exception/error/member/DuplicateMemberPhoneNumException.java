package com.example.demo.global.exception.error.member;

import com.example.demo.global.exception.ErrorCode;

public class DuplicateMemberPhoneNumException extends RuntimeException{
    public DuplicateMemberPhoneNumException() {
        super(ErrorCode.DUPLICATE_MEMBER_PHONE_NUM.getMessage());
    }
    public DuplicateMemberPhoneNumException(String message){
        super(message);
    }
    public DuplicateMemberPhoneNumException(String message, Throwable cause) {
        super(message, cause);
    }
    public DuplicateMemberPhoneNumException(Throwable cause){
        super(cause);
    }
}
