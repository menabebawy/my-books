package com.mybooks.api.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@Value
public class AuthorDTO {
    private String id;

    @NotBlank(message = "{author.firstName.required}")
    @NotNull(message = "{author.firstName.required}")
    @NotEmpty(message = "{author.firstName.required}")
    private String firstName;

    @NotBlank(message = "{author.lastName.required}")
    @NotNull(message = "{author.lastName.required}")
    @NotEmpty(message = "{author.lastName.required}")
    private String lastName;
}
