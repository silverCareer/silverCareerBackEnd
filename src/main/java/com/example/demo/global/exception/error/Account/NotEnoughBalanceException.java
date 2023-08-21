package com.example.demo.global.exception.error.Account;

import com.example.demo.global.exception.ErrorCode;

public class NotEnoughBalanceException extends RuntimeException{
    public NotEnoughBalanceException() { super(ErrorCode.NOT_FOUND_ACCOUNT.getMessage()); }
    public NotEnoughBalanceException(String message){ super(message);}
    public NotEnoughBalanceException(String message, Throwable cause) { super(message, cause); }
    public NotEnoughBalanceException(Throwable cause){ super(cause); }
}
