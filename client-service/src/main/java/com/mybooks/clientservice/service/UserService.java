package com.mybooks.oauthservice.service;

import com.mybooks.oauthservice.dto.AuthenticationRequestDTO;
import com.mybooks.oauthservice.exception.UserAlreadyExistException;
import com.mybooks.oauthservice.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User addUser(AuthenticationRequestDTO requestDTO) throws UserAlreadyExistException;
}
