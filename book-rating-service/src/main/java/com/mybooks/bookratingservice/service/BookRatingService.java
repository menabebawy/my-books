package com.mybooks.bookratingservice.service;

import com.mybooks.bookratingservice.dto.UserBookRatingRequestDto;
import com.mybooks.bookratingservice.dto.UserBooksRatingsResponseDto;
import com.mybooks.bookratingservice.exception.UserNotFoundException;

public interface BookRatingService {
    UserBooksRatingsResponseDto getBooksRatings(String userId) throws UserNotFoundException;

    void addBookRating(UserBookRatingRequestDto responseDto);
}
