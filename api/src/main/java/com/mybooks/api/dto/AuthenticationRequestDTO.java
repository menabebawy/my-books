package com.mybooks.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Value
@Builder
@AllArgsConstructor
public class AuthenticationRequestDTO {
    @Pattern(regexp = ".+[@].+[\\.].+", message = "{validation.email.wrong.pattern}")
    String email;

    @Size(min = 6, message = "{validation.password.size.too_short}")
    @NotBlank(message = "Password is empty or null")
    String password;
}
