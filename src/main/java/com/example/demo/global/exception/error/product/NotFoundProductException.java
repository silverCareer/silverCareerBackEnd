package com.example.demo.global.exception.error.product;

import com.example.demo.global.exception.ErrorCode;

public class NotFoundProductException extends RuntimeException{
    public NotFoundProductException() {
        super(ErrorCode.NOT_FOUND_PRODUCT_DETAIL.getMessage());
    }
    public NotFoundProductException(String message){
        super(message);
    }
    public NotFoundProductException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotFoundProductException(Throwable cause){
        super(cause);
    }
}
