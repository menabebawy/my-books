package com.mybooks.bookinfoservice.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class BookInfoResponseDto {
    String id;
    String title;
    AuthorDto author;
}
