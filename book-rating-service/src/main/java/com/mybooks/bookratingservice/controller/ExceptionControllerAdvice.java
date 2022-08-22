package com.mybooks.bookratingservice.controller;

import com.mybooks.bookratingservice.exception.BookNotFoundException;
import com.mybooks.bookratingservice.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BookNotFoundException.class)
    public String handleBookNotFoundException(BookNotFoundException ex) {
        return String.format("{ \"message\": \"%s\" }", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleBadRequestException(MethodArgumentNotValidException ex) {
        String error = ex.getBindingResult().getAllErrors()
                .stream()
                .findFirst()
                .map(ObjectError::getDefaultMessage)
                .get();
        return String.format("{ \"message\": \"%s\" }", error);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(UserNotFoundException ex) {
        return String.format("{ \"message\": \"%s\" }", ex.getMessage());
    }
}