package com.mybooks.clientservice.dto;

import com.mybooks.clientservice.annotation.PasswordValueMatch;
import com.mybooks.clientservice.annotation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotBlank;


@Value
@AllArgsConstructor
@PasswordValueMatch(
        field = "password",
        fieldMatch = "passwordConfirm",
        message = "Passwords do not match!"
)

public class ConfirmForgotPasswordRequestDto {
    @ValidPassword
    String password;

    @ValidPassword
    String passwordConfirm;

    @NotBlank
    String username;

    @NotBlank
    String confirmationCode;
}
