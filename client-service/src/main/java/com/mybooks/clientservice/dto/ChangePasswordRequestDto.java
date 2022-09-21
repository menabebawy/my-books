package com.mybooks.clientservice.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ChangePasswordRequestDto {
    String accessToken;
    String previousPassword;
    String proposedPassword;
}
