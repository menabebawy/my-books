package com.mybooks.clientservice.controller;

import com.mybooks.clientservice.exception.UserPasswordResetRequiredException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED)
    @ExceptionHandler(UserPasswordResetRequiredException.class)
    public String handlePasswordResetRequiredException(UserPasswordResetRequiredException ex) {
        return String.format("{ \"message\": \"%s\" }", ex.getMessage());
    }
}
