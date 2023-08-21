package com.example.demo.global.exception.error.suggestion;

import com.example.demo.global.exception.ErrorCode;

import java.util.NoSuchElementException;

public class NotFoundSuggestionsException extends NoSuchElementException {
    public NotFoundSuggestionsException() { super(ErrorCode.NOT_FOUND_SUGGESTIONS.getMessage()); }
    public NotFoundSuggestionsException(String message){ super(message);}
    public NotFoundSuggestionsException(String message, Throwable cause) { super(message, cause); }
    public NotFoundSuggestionsException(Throwable cause){ super(cause); }
}
