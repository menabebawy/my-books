package com.mybooks.bookratingservice;

import com.mybooks.bookratingservice.dto.UserBooksRatingsResponseDto;
import com.mybooks.bookratingservice.mapper.BookRatingMapper;
import com.mybooks.bookratingservice.mapper.BookRatingMapperImpl;
import com.mybooks.bookratingservice.model.UserBooksRatings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookRatingMapperTests {
    private BookRatingMapper bookRatingMapper;

    @BeforeEach
    void setup() {
        bookRatingMapper = new BookRatingMapperImpl();
    }

    @Test
    void givenUserBooksRatings_convertIntoUserBooksRatingsDto_thenCorrect() {
        Map<String, Integer> booksRatings = new HashMap<>();
        booksRatings.put("bookId", 1);

        UserBooksRatings userBooksRatings = new UserBooksRatings();
        userBooksRatings.setUserId("userId");

        userBooksRatings.setBooksRatings(booksRatings);

        UserBooksRatingsResponseDto dto = bookRatingMapper.transferToBookRatingResponseDto(userBooksRatings);

        assertEquals(userBooksRatings.getUserId(), dto.getUserId());
        assertEquals(userBooksRatings.getBooksRatings().size(), dto.getBooksRatings().size());
        assertEquals(userBooksRatings.getBooksRatings().get("bookId"), dto.getBooksRatings().get(0).getRate());
    }
}
