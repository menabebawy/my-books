package com.mybooks.bookratingservice.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Value
public class UserBookRatingRequestDto {
    @NotBlank(message = "{bookRating.userId.required}")
    String userId;

    @NotBlank(message = "{bookRating.bookId.required}")
    String bookId;

    @NotNull(message = "{bookRating.rate.required}")
    Integer rate;
}
