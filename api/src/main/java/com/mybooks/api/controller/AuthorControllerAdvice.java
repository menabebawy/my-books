package com.mybooks.api.controller;

import com.mybooks.api.exception.AuthorNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class AuthorControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<String> resourceNotFoundException(AuthorNotFoundException ex) {
        return new ResponseEntity<>("{ \"message\": \""+ex.getLocalizedMessage()+"\"}", HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> badRequestException(MethodArgumentNotValidException ex) {
        String error = ex.getBindingResult().getAllErrors()
                .stream()
                .findFirst()
                .map(ObjectError::getDefaultMessage)
                .get();

        return new ResponseEntity<>("{\"message\": \""+error+"\"}", HttpStatus.BAD_REQUEST);
    }
}
