package com.mybooks.api.dto;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(position = 1, required = true, value = "test@gmail.com")
    @Pattern(regexp = ".+[@].+[\\.].+", message = "{validation.email.wrong.pattern}")
    String email;

    @ApiModelProperty(position = 2, required = true, value = "P@SSWORD")
    @Size(min = 6, message = "{validation.password.size.too_short}")
    @NotBlank(message = "Password is empty or null")
    String password;
}
