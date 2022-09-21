package com.mybooks.clientservice.service;

import com.mybooks.clientservice.dto.*;
import com.mybooks.clientservice.service.awscognito.AwsCognitoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final AwsCognitoService awsCognitoService;

    @Override
    public AuthenticatedResponseDto login(LoginRequestDto request) {
        return awsCognitoService.login(request)
                .orElseThrow();
    }

    @Override
    public MessageResponseDto forgotPassword(String username) {
        return awsCognitoService.forgotPassword(username)
                .map(email -> MessageResponseDto.builder()
                        .message(String.format("You will get an email to %s soon.", email))
                        .build())
                .orElseThrow();
    }

    public MessageResponseDto confirmForgotPassword(ConfirmForgotPasswordRequestDto request) {
        return awsCognitoService.confirmForgotPassword(request)
                .map(notThing -> MessageResponseDto.builder()
                        .message("done!, try to login now")
                        .build())
                .orElseThrow();
    }

    public MessageResponseDto revokeToken(String token) {
        return awsCognitoService.revokeToken(token)
                .map(text -> MessageResponseDto.builder()
                        .message("Refresh token has been revoked")
                        .build())
                .orElseThrow();
    }

    public MessageResponseDto signup(SignupRequestDto request) {
        return awsCognitoService.signup(request)
                .map(userType -> MessageResponseDto.builder()
                        .message(request.getEmail() + " is created successfully")
                        .build())
                .orElseThrow();
    }
}