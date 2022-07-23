package com.mybooks.api.controller;

import com.mybooks.api.dto.AuthenticationRequestDTO;
import com.mybooks.api.dto.TokenResponseDTO;
import com.mybooks.api.exception.InvalidLoginException;
import com.mybooks.api.exception.UserAlreadyExistException;
import com.mybooks.api.service.UserService;
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
    public void signup(@Valid @RequestBody AuthenticationRequestDTO requestDTO) throws UserAlreadyExistException {
        userService.addUser(requestDTO);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponseDTO login(@Valid @RequestBody AuthenticationRequestDTO requestDTO) throws InvalidLoginException {
        return userService.login(requestDTO);
    }

    @GetMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponseDTO refreshToken(HttpServletRequest request) throws InvalidLoginException {
        return userService.refreshToken(request);
    }
}
