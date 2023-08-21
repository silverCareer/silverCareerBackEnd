package com.example.demo.global.exception.error.likes;

import com.example.demo.global.exception.ErrorCode;

public class NotFoundLikesException extends RuntimeException {
    public NotFoundLikesException() {
        super(ErrorCode.NOT_FOUND_LIKES.getMessage());
    }

    public NotFoundLikesException(String message) {
        super(message);
    }

    public NotFoundLikesException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundLikesException(Throwable cause) {
        super(cause);
    }
}
