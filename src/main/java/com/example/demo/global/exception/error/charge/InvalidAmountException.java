package com.example.demo.global.exception.error.charge;

import com.example.demo.global.exception.ErrorCode;

public class InvalidAmountException extends  RuntimeException{
    public InvalidAmountException() { super(ErrorCode.INVALID_AMOUNT_INPUT.getMessage()); }
    public InvalidAmountException(String message){
        super(message);
    }
    public InvalidAmountException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidAmountException(Throwable cause){
        super(cause);
    }
}
