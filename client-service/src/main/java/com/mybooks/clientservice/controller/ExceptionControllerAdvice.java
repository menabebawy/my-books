package com.mybooks.clientservice.controller;

import com.amazonaws.services.cognitoidp.model.*;
import com.mybooks.clientservice.dto.MessageResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PasswordResetRequiredException.class)
    public MessageResponseDto handlePasswordResetRequiredException() {
        return MessageResponseDto.builder()
                .message("New password is required, please request verification code firstly")
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LimitExceededException.class)
    public MessageResponseDto handleLimitExceededException() {
        return MessageResponseDto.builder()
                .message("Exceed limit for requesting, Please wait a while and try it later")
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExpiredCodeException.class)
    public MessageResponseDto handleExpiredCodeException() {
        return MessageResponseDto.builder()
                .message("Invalid code provided, please request a code again.")
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CodeMismatchException.class)
    public MessageResponseDto handleCodeMismatchException() {
        return MessageResponseDto.builder()
                .message("Invalid verification code provided")
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ValidationException.class,
            MethodArgumentNotValidException.class,
            InvalidParameterException.class})
    public void handleValidationException() {
    }

}
