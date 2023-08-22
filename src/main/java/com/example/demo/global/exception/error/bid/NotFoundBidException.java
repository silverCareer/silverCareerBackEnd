package com.example.demo.global.exception.error.bid;

import com.example.demo.global.exception.ErrorCode;

public class NotFoundBidException extends RuntimeException {
    public NotFoundBidException() {
        super(ErrorCode.NOT_FOUND_BID.getMessage());
    }
    public NotFoundBidException(String message){
        super(message);
    }
    public NotFoundBidException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotFoundBidException(Throwable cause){
        super(cause);
    }
}