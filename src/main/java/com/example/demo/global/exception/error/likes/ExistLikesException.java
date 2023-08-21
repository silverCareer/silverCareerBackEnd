package com.example.demo.global.exception.error.likes;

import com.example.demo.global.exception.ErrorCode;

public class ExistLikesException extends RuntimeException {
    public ExistLikesException() {
        super(ErrorCode.EXIST_LIKES.getMessage());
    }

    public ExistLikesException(String message) {
        super(message);
    }

    public ExistLikesException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistLikesException(Throwable cause) {
        super(cause);
    }

}
