package com.mybooks.api.service;

import com.mybooks.api.dto.SignupRequestDTO;
import com.mybooks.api.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    public User addUser(SignupRequestDTO signupRequestDto);
}
