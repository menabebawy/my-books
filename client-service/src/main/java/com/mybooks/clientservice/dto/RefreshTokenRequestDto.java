package com.mybooks.clientservice.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RefreshTokenRequestDto {
    String refreshToken;
    String username;
}
