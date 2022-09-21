package com.mybooks.clientservice.service;

import com.mybooks.clientservice.dto.*;

public interface UserService {
    AuthenticatedResponseDto login(LoginRequestDto request);

    MessageResponseDto forgotPassword(String username);

    MessageResponseDto confirmForgotPassword(ConfirmForgotPasswordRequestDto request);

    MessageResponseDto revokeToken(String token);

    MessageResponseDto signup(SignupRequestDto request);

    MessageResponseDto changePassword(ChangePasswordRequestDto request);
}
