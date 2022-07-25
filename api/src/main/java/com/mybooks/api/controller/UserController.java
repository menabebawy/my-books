package com.mybooks.api.controller;

import com.mybooks.api.dto.AuthenticationRequestDTO;
import com.mybooks.api.dto.TokenResponseDTO;
import com.mybooks.api.exception.InvalidLoginException;
import com.mybooks.api.exception.UserAlreadyExistException;
import com.mybooks.api.service.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful new used adding")})
    public void signup(
            @Parameter(description = "AuthenticationRequestDTO object that needs to add new user", required = true)
            @Valid @RequestBody AuthenticationRequestDTO requestDTO) throws UserAlreadyExistException {
        userService.addUser(requestDTO);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful user logging in")})
    public TokenResponseDTO login(
            @Parameter(description = "Credentials that needs to login the user", required = true)
            @Valid @RequestBody AuthenticationRequestDTO requestDTO) throws InvalidLoginException {
        return userService.login(requestDTO);
    }

    @GetMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful refresh token and access token getting")})
    public TokenResponseDTO refreshToken(HttpServletRequest request) throws InvalidLoginException {
        return userService.refreshToken(request);
    }
}
