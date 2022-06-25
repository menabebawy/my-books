package com.mybooks.api.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Builder
@Value
public class BookDTO {
    private String id;

    @NotBlank(message = "{book.title.required}")
    private String title;

    @NotBlank(message = "{book.authorId.required}")
    private String authorId;
}
