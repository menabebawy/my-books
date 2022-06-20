package com.mybooks.api.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@Value
public class BookDTO {
    private String id;

    @NotBlank(message = "{book.title.required}")
    @NotNull(message = "{book.title.required}")
    @NotEmpty(message = "{book.title.required}")
    private String title;

    @NotBlank(message = "{book.authorId.required}")
    @NotNull(message = "{book.authorId.required}")
    @NotEmpty(message = "{book.authorId.required}")
    private String authorId;
}
