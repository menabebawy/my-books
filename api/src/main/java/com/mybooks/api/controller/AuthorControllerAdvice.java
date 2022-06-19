package com.mybooks.api.controller;

import com.mybooks.api.exception.AuthorNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class AuthorControllerAdvice {

    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<String> resourceNotFoundException(AuthorNotFoundException ex) {
        return new ResponseEntity<>("{ \"message\": \""+ex.getLocalizedMessage()+"\"}", HttpStatus.NOT_FOUND);
    }
}
