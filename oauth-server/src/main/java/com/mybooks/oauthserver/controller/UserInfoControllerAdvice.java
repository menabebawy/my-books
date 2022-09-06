package com.mybooks.oauthserver.controller;

import com.mybooks.oauthserver.exception.InvalidLoginException;
import com.mybooks.oauthserver.exception.UserAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserInfoControllerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleBadRequestException(MethodArgumentNotValidException ex) {
        String error = ex.getBindingResult().getAllErrors()
                .stream()
                .findFirst()
                .map(ObjectError::getDefaultMessage)
                .orElse("");
        return generateErrorMessage(error);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistException.class)
    public String handleUserIsAlreadyExistException(UserAlreadyExistException ex) {
        return generateErrorMessage(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidLoginException.class)
    public String handleInvalidLoginException(InvalidLoginException ex) {
        return generateErrorMessage(ex.getMessage());
    }

    private String generateErrorMessage(String message) {
        return String.format("{ \"message\": \"%s\" }", message);
    }
}
