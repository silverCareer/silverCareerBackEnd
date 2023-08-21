package com.example.demo.global.exception.error.product;

import com.example.demo.global.exception.ErrorCode;

public class NotFoundProductListException extends RuntimeException {
    public NotFoundProductListException() {
        super(ErrorCode.NOT_FOUND_PRODUCT_LIST.getMessage());
    }
    public NotFoundProductListException(String message){
        super(message);
    }
    public NotFoundProductListException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotFoundProductListException(Throwable cause){
        super(cause);
    }
}
