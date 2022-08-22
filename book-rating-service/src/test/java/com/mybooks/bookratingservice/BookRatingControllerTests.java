package com.mybooks.bookratingservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybooks.bookratingservice.dto.UserBookRatingRequestDto;
import com.mybooks.bookratingservice.dto.UserBooksRatingsResponseDto;
import com.mybooks.bookratingservice.exception.UserNotFoundException;
import com.mybooks.bookratingservice.mapper.BookRatingMapper;
import com.mybooks.bookratingservice.service.BookRatingServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookRatingControllerTests {
    private final String baseUrl = "/book/rating";

    private final String userId = "UserId";
    private final String bookId = "BookId";
    private final Integer rate = 1;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    BookRatingServiceImpl bookRatingService;
    @Autowired
    BookRatingMapper bookRatingMapper;

    @Test
    void whenGetUserBooksRatings_GivenUserId_thenCorrect() throws Exception {
        Map<String, Integer> booksRatings = new HashMap<>();
        booksRatings.put(bookId, rate);

        UserBooksRatingsResponseDto responseDto = UserBooksRatingsResponseDto.builder()
                .userId(userId)
                .booksRatings(booksRatings)
                .build();
        when(bookRatingService.getBooksRatings(userId)).thenReturn(responseDto);

        mockMvc.perform(get(baseUrl + "/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.userId", Matchers.is(userId)))
                .andExpect(jsonPath("$.booksRatings[0].bookId", Matchers.is(bookId)))
                .andExpect(jsonPath("$.booksRatings[0].rate", Matchers.is(rate)));
    }

    @Test
    void whenGetUserBooksRatings_givenNotFoundUser_thenCorrect() throws Exception {
        when(bookRatingService.getBooksRatings(userId)).thenThrow(new UserNotFoundException(userId));

        mockMvc.perform(get(baseUrl + "/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("message", Matchers.is("Could not find user " + userId)));
    }

    @Test
    void whenPostBookRating_givenBadRequest_thenCorrect() throws Exception {
        UserBookRatingRequestDto responseDto = UserBookRatingRequestDto.builder()
                .userId(userId)
                .bookId(null)
                .rate(1)
                .build();

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(responseDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("message", Matchers.is("BookId is required")));
    }

    @Test
    void whenPostUserBookRating_whenValidRequest_thenCorrect() throws Exception {
        UserBookRatingRequestDto responseDto = UserBookRatingRequestDto.builder()
                .userId(userId)
                .bookId(bookId)
                .rate(1)
                .build();

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(responseDto)))
                .andExpect(status().isCreated());
    }
}
