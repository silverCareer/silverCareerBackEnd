package com.example.demo.global.exception.error.product;

import com.example.demo.global.exception.ErrorCode;

public class InvalidProductInfoException extends RuntimeException{
    public InvalidProductInfoException() {
        super(ErrorCode.INVALID_PRODUCT_INFO.getMessage());
    }
    public InvalidProductInfoException(String message){
        super(message);
    }
    public InvalidProductInfoException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidProductInfoException(Throwable cause){
        super(cause);
    }
}
