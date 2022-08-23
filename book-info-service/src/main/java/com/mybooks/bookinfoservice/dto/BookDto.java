package com.mybooks.bookinfoservice.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@Builder
public class BookDto {
    String id;

    @NotBlank(message = "{book.title.required}")
    String title;

    @NotBlank(message = "{book.authorId.required}")
    String authorId;
}
