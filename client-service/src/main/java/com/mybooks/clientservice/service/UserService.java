package com.mybooks.clientservice.service;

import com.mybooks.clientservice.dto.LoginRequestDto;
import com.mybooks.clientservice.dto.LoginResponseDto;

public interface UserService {
    LoginResponseDto login(LoginRequestDto request);
}
