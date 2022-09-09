package com.mybooks.clientservice.controller;

import com.mybooks.clientservice.dto.AuthenticationRequestDTO;
import com.mybooks.clientservice.exception.UserAlreadyExistException;
import com.mybooks.clientservice.service.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String hello() {
        return "hello";
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful new used adding")})
    public void signup(
            @Parameter(description = "AuthenticationRequestDTO object that needs to add new user", required = true)
            @Valid @RequestBody AuthenticationRequestDTO requestDTO) throws UserAlreadyExistException {
        userService.addUser(requestDTO);
    }
}
