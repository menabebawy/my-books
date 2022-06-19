package com.mybooks.api.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Builder
@Value
public class AuthorDTO {
    private String id;
    private String firstName;
    private String lastName;
}
