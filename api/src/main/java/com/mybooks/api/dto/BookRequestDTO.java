package com.mybooks.api.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Builder
@Value
public class BookRequestDTO {
    @NotBlank(message = "{book.title.required}")
    String title;

    @NotBlank(message = "{book.authorId.required}")
    String authorId;
}
