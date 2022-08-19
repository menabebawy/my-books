package com.mybooks.authorinfoservice.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Builder
@Value
public class AuthorDTO {
    String id;

    @NotBlank(message = "{author.firstName.required}")
    String firstName;

    @NotBlank(message = "{author.lastName.required}")
    String lastName;
}
