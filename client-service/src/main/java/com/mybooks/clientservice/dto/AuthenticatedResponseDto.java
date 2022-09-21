package com.mybooks.clientservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class AuthenticatedResponseDto {
    String accessToken;
    String idToken;
    String refreshToken;
    Integer expiresIn;
    String tokenType;
    String username;
}
