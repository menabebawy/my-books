package com.mybooks.clientservice.controller;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class BookDTO {
    String id;

    // @NotBlank(message = "{book.title.required}")
    String title;

    //  @NotBlank(message = "{book.authorId.required}")
    String authorId;
}
