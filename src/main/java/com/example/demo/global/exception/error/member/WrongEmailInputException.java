package com.example.demo.global.exception.error.member;

import com.example.demo.global.exception.ErrorCode;

public class WrongEmailInputException extends RuntimeException{
    public WrongEmailInputException() {
        super(ErrorCode.WRONG_EMAIL_INPUT.getMessage());
    }
    public WrongEmailInputException(String message){
        super(message);
    }
    public WrongEmailInputException(String message, Throwable cause) {
        super(message, cause);
    }
    public WrongEmailInputException(Throwable cause){
        super(cause);
    }
}
