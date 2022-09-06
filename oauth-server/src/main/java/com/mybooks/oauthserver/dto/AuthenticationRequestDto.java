package com.mybooks.oauthserver.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Data
@SuperBuilder
public class AuthenticationRequestDto {
    @ApiModelProperty(position = 1, required = true, value = "test@gmail.com")
    @Pattern(regexp = ".+[@].+[\\.].+", message = "{validation.email.wrong.pattern}")
    String email;

    @ApiModelProperty(position = 2, required = true, value = "P@SSWORD")
    @Size(min = 6, message = "{validation.password.size.too_short}")
    @NotBlank(message = "Password is empty or null")
    String password;
}
