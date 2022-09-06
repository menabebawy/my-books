package com.mybooks.bookcatalogservice.controller;

import com.mybooks.bookcatalogservice.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BookCatalogControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public String handleAuthorNotFoundException(UserNotFoundException ex) {
        return String.format("{ \"message\": \"%s\" }", ex.getMessage());
    }
}
