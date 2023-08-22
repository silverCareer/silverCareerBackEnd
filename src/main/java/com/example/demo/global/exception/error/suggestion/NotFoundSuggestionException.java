package com.example.demo.global.exception.error.suggestion;

import com.example.demo.global.exception.ErrorCode;

public class NotFoundSuggestionException extends RuntimeException {
    public NotFoundSuggestionException() {
        super(ErrorCode.NOT_FOUND_SUGGESTION.getMessage());
    }
    public NotFoundSuggestionException(String message){
        super(message);
    }
    public NotFoundSuggestionException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotFoundSuggestionException(Throwable cause){
        super(cause);
    }
}
