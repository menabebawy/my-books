package com.mybooks.bookratingservice;

import com.mybooks.bookratingservice.dto.UserBookRatingRequestDto;
import com.mybooks.bookratingservice.dto.UserBooksRatingsResponseDto;
import com.mybooks.bookratingservice.exception.UserNotFoundException;
import com.mybooks.bookratingservice.mapper.BookRatingMapper;
import com.mybooks.bookratingservice.mapper.BookRatingMapperImpl;
import com.mybooks.bookratingservice.model.UserBooksRatings;
import com.mybooks.bookratingservice.repository.BookRatingRepository;
import com.mybooks.bookratingservice.service.BookRatingService;
import com.mybooks.bookratingservice.service.BookRatingServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BookRatingServiceTests {
    private final String userId = "userId";
    private final String bookId = "bookId";
    private final Integer rate = 1;
    @Mock
    BookRatingRepository repository;
    private BookRatingMapper mapper;
    private BookRatingService service;

    @BeforeEach
    void setup() {
        mapper = new BookRatingMapperImpl();
        service = new BookRatingServiceImpl(repository, mapper);
    }

    @AfterEach
    void destroyAll() {
        repository.deleteAll();
    }

    @Test
    void whenGivenUserIdAndRequestBooksRatings_thenCorrect() {
        UserBooksRatings userBooksRatings = new UserBooksRatings();
        userBooksRatings.setUserId(userId);
        Map<String, Integer> booksRatings = new HashMap<>();
        booksRatings.put(bookId, rate);
        userBooksRatings.setBooksRatings(booksRatings);

        UserBooksRatingsResponseDto dto = mapper.transferToBookRatingResponseDto(userBooksRatings);

        when(repository.findById(userId)).thenReturn(Optional.of(userBooksRatings));
        assertDoesNotThrow(() -> service.getBooksRatings(userId));

        UserBooksRatingsResponseDto returnDto = service.getBooksRatings(userId);

        assertEquals(returnDto.getUserId(), userId);
        assertEquals(returnDto.getBooksRatings().size(), 1);
        assertEquals(returnDto.getBooksRatings().get(0).getBookId(), bookId);
        assertEquals(returnDto.getBooksRatings().get(0).getRate(), rate);
    }

    @Test
    void whenRequestUserBooksRatings_userNotFound_thenCorrect() {
        when(repository.findById(userId)).thenThrow(new UserNotFoundException(userId));
        assertThrows(UserNotFoundException.class, () -> service.getBooksRatings(userId));
        verify(repository, Mockito.times(1)).findById(userId);
    }

    @Test
    void whenAddBookRating_thenCorrect() {
        UserBooksRatings userBooksRatings = new UserBooksRatings();
        userBooksRatings.setUserId(userId);
        Map<String, Integer> booksRatings = new HashMap<>();
        booksRatings.put(bookId, rate);
        userBooksRatings.setBooksRatings(booksRatings);

        when(repository.save(ArgumentMatchers.any(UserBooksRatings.class))).thenReturn(userBooksRatings);

        UserBookRatingRequestDto requestDto = UserBookRatingRequestDto.builder()
                .userId(userId)
                .bookId(bookId)
                .rate(rate)
                .build();
        
        when(repository.findById(userId)).thenReturn(Optional.of(userBooksRatings));
        assertDoesNotThrow(() -> service.addBookRating(requestDto));
    }
}
