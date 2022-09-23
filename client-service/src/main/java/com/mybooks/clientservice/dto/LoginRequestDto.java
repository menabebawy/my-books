package com.mybooks.clientservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@Builder
@AllArgsConstructor
public class LoginRequestDto {
    @NotBlank
    String username;

    @NotBlank
    String password;
}
