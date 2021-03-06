package com.mybooks.api.controller;

import com.mybooks.api.exception.AuthorNotFoundException;
import com.mybooks.api.exception.BookNotFoundException;
import com.mybooks.api.exception.InvalidLoginException;
import com.mybooks.api.exception.UserAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AuthorNotFoundException.class)
    public String handleAuthorNotFoundException(AuthorNotFoundException ex) {
        return String.format("{ \"message\": \"%s\" }", ex.getMessage());
    }

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

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistException.class)
    public String handleUserIsAlreadyExistException(UserAlreadyExistException ex) {
        return String.format("{ \"message\": \"%s\" }", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidLoginException.class)
    public String handleInvalidLoginException(InvalidLoginException ex) {
        return String.format("{ \"message\": \"%s\" }", ex.getMessage());
    }
}