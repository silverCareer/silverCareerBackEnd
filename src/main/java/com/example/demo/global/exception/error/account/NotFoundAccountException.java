package com.example.demo.global.exception.error.account;

import com.example.demo.global.exception.ErrorCode;

public class NotFoundAccountException extends RuntimeException {
    public NotFoundAccountException() { super(ErrorCode.NOT_FOUND_ACCOUNT.getMessage()); }
    public NotFoundAccountException(String message){
        super(message);
    }
    public NotFoundAccountException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotFoundAccountException(Throwable cause){
        super(cause);
    }
}
