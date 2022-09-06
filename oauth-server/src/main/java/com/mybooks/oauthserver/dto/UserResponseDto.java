package com.mybooks.oauthserver.dto;

import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Builder
@Value
public class UserResponseDto {
    String id;
    String email;
    String name;
    Set<String> roles;
}