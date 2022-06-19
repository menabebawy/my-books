package com.mybooks.api.controller;

import com.mybooks.api.exception.BookNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BookControllerAdvice {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<String> resourceNotFoundException(BookNotFoundException ex) {
        return new ResponseEntity<>("{ \"message\": \""+ex.getLocalizedMessage()+"\"}", HttpStatus.NOT_FOUND);
    }
}
