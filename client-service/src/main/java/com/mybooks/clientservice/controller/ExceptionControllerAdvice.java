package com.mybooks.clientservice.controller;

import com.amazonaws.services.cognitoidp.model.ExpiredCodeException;
import com.amazonaws.services.cognitoidp.model.InvalidParameterException;
import com.amazonaws.services.cognitoidp.model.LimitExceededException;
import com.mybooks.clientservice.dto.MessageResponseDto;
import com.mybooks.clientservice.exception.UserPasswordResetRequiredException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserPasswordResetRequiredException.class)
    public MessageResponseDto handlePasswordResetRequiredException(UserPasswordResetRequiredException exception) {
        return MessageResponseDto.builder()
                .message(exception.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LimitExceededException.class)
    public MessageResponseDto handleLimitExceededException() {
        return MessageResponseDto.builder()
                .message("Exceeded limit for requesting new verification code, Please wait for a while and try later")
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
    @ExceptionHandler(InvalidParameterException.class)
    public MessageResponseDto handleInvalidParameterException() {
        return MessageResponseDto.builder()
                .message("Invalid parameter")
                .build();
    }
}
