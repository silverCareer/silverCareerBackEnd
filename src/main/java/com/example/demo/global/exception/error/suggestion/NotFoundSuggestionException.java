package com.example.demo.global.exception.error.suggestion;

import com.example.demo.global.exception.ErrorCode;

import java.util.NoSuchElementException;

public class NotFoundSuggestionException extends NoSuchElementException {
    public NotFoundSuggestionException() { super(ErrorCode.NOT_FOUND_SUGGESTIONS.getMessage()); }
    public NotFoundSuggestionException(String message){ super(message);}
    public NotFoundSuggestionException(String message, Throwable cause) { super(message, cause); }
    public NotFoundSuggestionException(Throwable cause){ super(cause); }
}
