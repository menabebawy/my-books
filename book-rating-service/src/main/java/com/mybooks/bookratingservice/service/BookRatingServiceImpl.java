package com.mybooks.bookratingservice.service;

import com.mybooks.bookratingservice.dto.UserBookRatingRequestDto;
import com.mybooks.bookratingservice.dto.UserBooksRatingsResponseDto;
import com.mybooks.bookratingservice.exception.UserNotFoundException;
import com.mybooks.bookratingservice.mapper.BookRatingMapper;
import com.mybooks.bookratingservice.repository.BookRatingRepository;
import org.springframework.stereotype.Service;

@Service
public class BookRatingServiceImpl implements BookRatingService {
    private final BookRatingRepository repository;
    private final BookRatingMapper mapper;

    public BookRatingServiceImpl(BookRatingRepository repository, BookRatingMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public UserBooksRatingsResponseDto getBooksRatings(String userId) throws UserNotFoundException {
        return repository.findById(userId)
                .map(mapper::transferToBookRatingResponseDto)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public void addBookRating(UserBookRatingRequestDto requestDto) {
        // =======================================================================================
        // we need to ask book info if there is a book with upcoming bookId, but let's put it off
        // ========================================================================================

        repository.findById(requestDto.getUserId())
                .map(userBooksRatings -> {
                    userBooksRatings.getBooksRatings().put(requestDto.getBookId(), requestDto.getRate());
                    return userBooksRatings;
                })
                .map(repository::save)
                .orElseThrow(() -> new UserNotFoundException(requestDto.getUserId()));
    }
}
