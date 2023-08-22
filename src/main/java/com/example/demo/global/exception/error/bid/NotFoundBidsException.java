package com.example.demo.global.exception.error.bid;

import com.example.demo.global.exception.ErrorCode;

import java.util.NoSuchElementException;

public class NotFoundBidsException extends RuntimeException {
    public NotFoundBidsException() {
        super(ErrorCode.NOT_FOUND_BID.getMessage());
    }
    public NotFoundBidsException(String message){
        super(message);
    }
    public NotFoundBidsException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotFoundBidsException(Throwable cause){
        super(cause);
    }
}
