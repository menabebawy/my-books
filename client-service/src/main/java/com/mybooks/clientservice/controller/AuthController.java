package com.mybooks.clientservice.controller;

import com.mybooks.clientservice.dto.BaseResponseDto;
import com.mybooks.clientservice.dto.LoginRequestDto;
import com.mybooks.clientservice.dto.LoginResponseDto;
import com.mybooks.clientservice.dto.SignupRequestDto;
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
    public BaseResponseDto signup(@RequestBody @Valid SignupRequestDto request) {
        return null;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseDto login(@RequestBody @Valid LoginRequestDto request) {
        return userService.login(request);
    }

}
