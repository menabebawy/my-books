package com.mybooks.bookratingservice.mapper;

import com.mybooks.bookratingservice.dto.UserBooksRatingsResponseDto;
import com.mybooks.bookratingservice.model.UserBooksRatings;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookRatingMapper {
    UserBooksRatingsResponseDto transferToBookRatingResponseDto(UserBooksRatings bookRating);
}