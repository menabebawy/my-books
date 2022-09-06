package com.mybooks.bookcatalogservice.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class BookCatalogResponseDto {
    List<BookCatalogDto> bookCatalogDtoList;
}
