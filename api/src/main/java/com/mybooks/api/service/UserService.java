package com.mybooks.api.service;

import com.mybooks.api.dto.AuthenticationRequestDTO;
import com.mybooks.api.dto.TokenResponseDTO;
import com.mybooks.api.exception.InvalidLoginException;
import com.mybooks.api.exception.InvalidRefreshTokenException;
import com.mybooks.api.exception.UserAlreadyExistException;
import com.mybooks.api.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;

public interface UserService extends UserDetailsService {
    User addUser(AuthenticationRequestDTO requestDTO) throws UserAlreadyExistException;

    TokenResponseDTO login(AuthenticationRequestDTO requestDTO) throws InvalidLoginException;

    TokenResponseDTO refreshToken(HttpServletRequest httpServletRequest) throws InvalidRefreshTokenException;
}
