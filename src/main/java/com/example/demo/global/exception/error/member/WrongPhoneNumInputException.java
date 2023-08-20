package com.example.demo.global.exception.error.member;

import com.example.demo.global.exception.ErrorCode;

public class WrongPhoneNumInputException extends RuntimeException{
    public WrongPhoneNumInputException() {
        super(ErrorCode.WRONG_PHONE_NUM_INPUT.getMessage());
    }
    public WrongPhoneNumInputException(String message){
        super(message);
    }
    public WrongPhoneNumInputException(String message, Throwable cause) {
        super(message, cause);
    }
    public WrongPhoneNumInputException(Throwable cause){
        super(cause);
    }
}
