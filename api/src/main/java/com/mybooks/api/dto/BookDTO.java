package com.mybooks.api.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class BookDTO {
    private String id;
    private String title;
    private String authorId;
}
