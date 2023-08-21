package com.example.demo.global.exception.error.member;

import com.example.demo.global.exception.ErrorCode;

public class WrongPasswordInputException extends RuntimeException{
    public WrongPasswordInputException() {
        super(ErrorCode.WRONG_PASSWORD_INPUT.getMessage());
    }
    public WrongPasswordInputException(String message){
        super(message);
    }
    public WrongPasswordInputException(String message, Throwable cause) {
        super(message, cause);
    }
    public WrongPasswordInputException(Throwable cause){
        super(cause);
    }
}
