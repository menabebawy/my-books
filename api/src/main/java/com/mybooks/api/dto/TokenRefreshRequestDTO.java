package com.mybooks.api.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@Builder
public class TokenRefreshRequestDTO {
    @NotBlank
    String refreshToken;
}
