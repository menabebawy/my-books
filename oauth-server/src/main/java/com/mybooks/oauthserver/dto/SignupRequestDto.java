package com.mybooks.oauthserver.dto;

import lombok.Value;
import lombok.experimental.SuperBuilder;

@Value
@SuperBuilder
public class SignupRequestDto extends AuthenticationRequestDto {
    String name;
}
