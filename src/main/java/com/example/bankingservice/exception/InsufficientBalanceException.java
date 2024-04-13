package com.example.bankingservice.exception;

public class InsufficientBalanceException extends Throwable {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
