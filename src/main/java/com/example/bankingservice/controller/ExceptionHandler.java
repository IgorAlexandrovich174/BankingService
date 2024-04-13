package com.example.bankingservice.controller;

import com.example.bankingservice.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<String> handle(ValidationException validationException){
        return new ResponseEntity<>(validationException.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
