package com.mybooks.clientservice.service;

import com.mybooks.clientservice.dto.AuthenticationRequestDTO;
import com.mybooks.clientservice.exception.UserAlreadyExistException;
import com.mybooks.clientservice.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User addUser(AuthenticationRequestDTO requestDTO) throws UserAlreadyExistException;
}
