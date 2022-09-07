package com.mybooks.resourceservice.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@Builder
public class AuthorRequestDTO {
    @ApiModelProperty(position = 1, required = true, value = "Tim")
    @NotBlank(message = "{author.firstName.required}")
    String firstName;

    @ApiModelProperty(position = 1, required = true, value = "Heinzl")
    @NotBlank(message = "{author.lastName.required}")
    String lastName;
}
