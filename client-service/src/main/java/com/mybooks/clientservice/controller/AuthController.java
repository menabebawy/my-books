package com.mybooks.clientservice.controller;

import com.mybooks.clientservice.dto.*;
import com.mybooks.clientservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("signup")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDto signup(@RequestBody @Valid SignupRequestDto request) {
        return userService.signup(request);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticatedResponseDto login(@RequestBody @Valid LoginRequestDto request) {
        return userService.login(request);
    }

    @GetMapping("/forgot-password")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponseDto forgotPassword(@RequestParam @Valid String username) {
        return userService.forgotPassword(username);
    }

    @PostMapping("/confirm-forgot-password")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponseDto confirmForgotPassword(@RequestBody @Valid ConfirmForgotPasswordRequestDto request) {
        return userService.confirmForgotPassword(request);
    }

    @GetMapping("/revoke-token")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponseDto revokeToken(@RequestParam @Valid String token) {
        return userService.revokeToken(token);
    }

    @PostMapping("/change-password")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public MessageResponseDto changePassword(@RequestBody @Valid ChangePasswordRequestDto request) {
        return userService.changePassword(request);
    }
}