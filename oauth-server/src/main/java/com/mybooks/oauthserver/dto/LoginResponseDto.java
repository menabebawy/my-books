package com.mybooks.oauthserver.dto;

import com.mybooks.oauthserver.model.User;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class LoginResponseDto {
    User user;
    TokenResponseDto tokenResponseDto;
}
