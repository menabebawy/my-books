package com.mybooks.api.controller;

import com.mybooks.api.dto.LoginRequestDto;
import com.mybooks.api.dto.TokenResponseDTO;
import com.mybooks.api.exception.InvalidLoginException;
import com.mybooks.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class LoginController {
    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponseDTO login(@Valid @RequestBody LoginRequestDto loginRequestDto) throws InvalidLoginException {
        return userService.login(loginRequestDto);
    }

    @GetMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponseDTO refreshToken(HttpServletRequest request) throws InvalidLoginException {
        return userService.refreshToken(request);
    }
}
