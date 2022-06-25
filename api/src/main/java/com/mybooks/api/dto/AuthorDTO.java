package com.mybooks.api.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Builder
@Value
public class AuthorDTO {
    private String id;

    @NotBlank(message = "{author.firstName.required}")
    private String firstName;

    @NotBlank(message = "{author.lastName.required}")
    private String lastName;
}
