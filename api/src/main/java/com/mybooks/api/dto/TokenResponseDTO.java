package com.mybooks.api.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TokenResponseDTO {
    private String accessToken;
    private String refreshToken;
}
