package com.mybooks.clientservice.dto;

import com.mybooks.clientservice.annotation.ValidPassword;
import com.mybooks.clientservice.annotation.ValidPhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Value
@Builder
@AllArgsConstructor
public class SignupRequestDto {
    @NotBlank
    @Email
    String email;

    @ValidPassword
    String password;

    @NotBlank
    String firstName;

    @NotBlank
    String lastName;

    @ValidPhoneNumber
    String phoneNumber;

    @NotBlank
    Set<String> roles;
}
