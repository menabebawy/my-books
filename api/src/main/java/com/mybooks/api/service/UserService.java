package com.mybooks.api.service;

import com.mybooks.api.dto.LoginRequestDto;
import com.mybooks.api.dto.SignupRequestDTO;
import com.mybooks.api.dto.TokenResponseDTO;
import com.mybooks.api.exception.InvalidLoginException;
import com.mybooks.api.exception.InvalidRefreshTokenException;
import com.mybooks.api.exception.UserAlreadyExistException;
import com.mybooks.api.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;

public interface UserService extends UserDetailsService {
    User addUser(SignupRequestDTO signupRequestDto) throws UserAlreadyExistException;

    TokenResponseDTO login(LoginRequestDto loginRequestDto) throws InvalidLoginException;

    TokenResponseDTO refreshToken(HttpServletRequest httpServletRequest) throws InvalidRefreshTokenException;
}
