package com.mybooks.oauthserver.controller;

import com.mybooks.oauthserver.dto.*;
import com.mybooks.oauthserver.exception.InvalidLoginException;
import com.mybooks.oauthserver.exception.UserAlreadyExistException;
import com.mybooks.oauthserver.service.UserInfoService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserInfoController {
    private final UserInfoService userInfoService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Successful new used adding")})
    public UserResponseDto signup(
            @Parameter(description = "SignupRequestDto object that needs to add new user", required = true)
            @RequestBody SignupRequestDto signupRequestDto
    ) throws UserAlreadyExistException {
        return userInfoService.addUser(signupRequestDto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseDto login(
            @RequestBody AuthenticationRequestDto authenticationRequestDto
    ) throws InvalidLoginException {
        return userInfoService.login(authenticationRequestDto);
    }

    @GetMapping("/token")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful refresh token and access token getting")})
    public TokenResponseDto refreshToken(HttpServletRequest request) throws InvalidLoginException {
        return userInfoService.refreshToken(request);
    }
}
