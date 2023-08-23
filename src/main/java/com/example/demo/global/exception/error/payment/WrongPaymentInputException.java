package com.example.demo.global.exception.error.payment;

import com.example.demo.global.exception.ErrorCode;

public class WrongPaymentInputException extends RuntimeException{
    public WrongPaymentInputException() {
        super(ErrorCode.NOT_ENOUGH_MEMBER_BALANCE.getMessage());
    }
    public WrongPaymentInputException(String message){
        super(message);
    }
    public WrongPaymentInputException(String message, Throwable cause) {
        super(message, cause);
    }
    public WrongPaymentInputException(Throwable cause){
        super(cause);
    }
}
