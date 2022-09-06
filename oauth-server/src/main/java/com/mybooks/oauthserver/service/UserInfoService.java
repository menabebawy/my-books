package com.mybooks.oauthserver.service;

import com.mybooks.oauthserver.dto.*;
import com.mybooks.oauthserver.exception.InvalidLoginException;
import com.mybooks.oauthserver.exception.InvalidRefreshTokenException;
import com.mybooks.oauthserver.exception.UserAlreadyExistException;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;

public interface UserInfoService extends UserDetailsService {
    UserResponseDto addUser(SignupRequestDto requestDTO) throws UserAlreadyExistException;

    LoginResponseDto login(AuthenticationRequestDto requestDTO) throws InvalidLoginException;

    TokenResponseDto refreshToken(HttpServletRequest httpServletRequest) throws InvalidRefreshTokenException;
}
