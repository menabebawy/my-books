package com.mybooks.clientservice.controller;

import com.amazonaws.services.cognitoidp.model.*;
import com.mybooks.clientservice.dto.MessageResponseDto;
import com.mybooks.clientservice.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotAuthorizedException.class)
    public MessageResponseDto handleNotAuthorizedException() {
        return messageResponseDto("Incorrect username or password.");

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PasswordResetRequiredException.class)
    public MessageResponseDto handlePasswordResetRequiredException() {
        return messageResponseDto("New password is required, please request verification code firstly");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            ValidationException.class,
            ServiceException.class,
            MethodArgumentNotValidException.class,
            InvalidParameterException.class,
    })
    public MessageResponseDto handleHttpMessageNotReadableException() {
        return messageResponseDto("Parameter is missing or invalid.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public MessageResponseDto handleMissingServletRequestParameterException(
            MissingServletRequestParameterException exception) {
        return messageResponseDto(exception.getParameterName() + " is missing with type of " + exception.getParameterType());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LimitExceededException.class)
    public MessageResponseDto handleLimitExceededException() {
        return messageResponseDto("Exceed limit for requesting, Please wait a while and try it later");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExpiredCodeException.class)
    public MessageResponseDto handleExpiredCodeException() {
        return messageResponseDto("Invalid verification code, please request a code again.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CodeMismatchException.class)
    public MessageResponseDto handleCodeMismatchException() {
        return messageResponseDto("Invalid verification code");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameExistsException.class)
    public MessageResponseDto handleUsernameExistsException(UsernameExistsException exception) {
        return messageResponseDto(String.format("User name %s is already exists", exception.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidPasswordException.class)
    public MessageResponseDto handleInvalidPasswordException() {
        return messageResponseDto("Invalid password");
    }

    private MessageResponseDto messageResponseDto(String message) {
        return MessageResponseDto.builder().message(message).build();
    }
}
