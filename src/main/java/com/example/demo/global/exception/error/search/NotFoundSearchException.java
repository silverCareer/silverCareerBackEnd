package com.example.demo.global.exception.error.search;

import com.example.demo.global.exception.ErrorCode;

public class NotFoundSearchException extends RuntimeException{
    public NotFoundSearchException() {
        super(ErrorCode.NOT_FOUND_SEARCH_LIST.getMessage());
    }
    public NotFoundSearchException(String message){
        super(message);
    }
    public NotFoundSearchException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotFoundSearchException(Throwable cause){
        super(cause);
    }
}
