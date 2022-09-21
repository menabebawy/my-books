package com.mybooks.clientservice.service.awscognito;

import com.mybooks.clientservice.dto.AuthenticatedResponseDto;
import com.mybooks.clientservice.dto.ConfirmForgotPasswordRequestDto;
import com.mybooks.clientservice.dto.LoginRequestDto;

import java.util.Optional;

public interface AwsCognitoService {
    Optional<AuthenticatedResponseDto> login(LoginRequestDto request);

    Optional<String> forgotPassword(String username);

    Optional<String> confirmForgotPassword(ConfirmForgotPasswordRequestDto request);

    public Optional<String> revokeToken(String token);
}
