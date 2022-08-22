package com.mybooks.bookratingservice.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class BookRatingDto {
    String bookId;
    Integer rate;
}
