package com.mybooks.bookratingservice.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Value
@Builder
public class UserBooksRatingsResponseDto {
    String userId;

    Map<String, Integer> booksRatings;

    public List<BookRatingDto> getBooksRatings() {
        return booksRatings.entrySet()
                .stream()
                .map(element ->
                        BookRatingDto.builder().bookId(element.getKey()).rate(element.getValue()).build()
                )
                .collect(Collectors.toList());
    }
}
