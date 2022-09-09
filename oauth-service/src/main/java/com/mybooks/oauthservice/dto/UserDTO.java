package com.mybooks.clientservice.dto;

import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Builder
@Value
public class UserDTO {
    String id;
    String name;
    String email;
    String password;
    Set<String> roles;
}
