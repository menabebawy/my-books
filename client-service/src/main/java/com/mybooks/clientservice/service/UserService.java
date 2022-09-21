package com.mybooks.clientservice.service;

import com.mybooks.clientservice.dto.AuthenticatedResponseDto;
import com.mybooks.clientservice.dto.ConfirmForgotPasswordRequestDto;
import com.mybooks.clientservice.dto.LoginRequestDto;
import com.mybooks.clientservice.dto.MessageResponseDto;

public interface UserService {
    AuthenticatedResponseDto login(LoginRequestDto request);

    MessageResponseDto forgotPassword(String username);

    MessageResponseDto confirmForgotPassword(ConfirmForgotPasswordRequestDto request);

    MessageResponseDto revokeToken(String token);
}
