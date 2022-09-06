package com.mybooks.oauthserver.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class TokenResponseDto {
    String refreshToken;
    String accessToken;
}
