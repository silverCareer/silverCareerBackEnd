package com.example.demo.global.exception.error.payment;

import com.example.demo.global.exception.ErrorCode;

public class NotFoundPaymentHistoryException extends RuntimeException{
    public NotFoundPaymentHistoryException() {
        super(ErrorCode.NOT_FOUND_PAYMENT_HISTORY.getMessage());
    }
    public NotFoundPaymentHistoryException(String message){
        super(message);
    }
    public NotFoundPaymentHistoryException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotFoundPaymentHistoryException(Throwable cause){
        super(cause);
    }
}
