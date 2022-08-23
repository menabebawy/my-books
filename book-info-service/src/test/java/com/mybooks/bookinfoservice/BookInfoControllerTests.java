package com.mybooks.bookinfoservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybooks.bookinfoservice.dto.AuthorDto;
import com.mybooks.bookinfoservice.dto.BookInfoResponseDto;
import com.mybooks.bookinfoservice.exception.BookNotFoundException;
import com.mybooks.bookinfoservice.mapper.BookInfoMapper;
import com.mybooks.bookinfoservice.service.BookInfoServiceImpl;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookInfoControllerTests {
    private final String baseUrl = "/book";
    private final String bookId = "bookId";
    private final String title = "title";
    private final String authorId = "authorId";
    private final String firstName = "firstName";
    private final String lastName = "lastName";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookInfoServiceImpl service;
    @Autowired
    private BookInfoMapper bookInfoMapper;

    @Test
    void whenGetBookInfo_givenBookId_thenCorrect() throws Exception {
        AuthorDto authorDto = AuthorDto.builder()
                .id(authorId)
                .firstName(firstName)
                .lastName(lastName)
                .build();
        BookInfoResponseDto responseDto = BookInfoResponseDto.builder()
                .id(bookId)
                .title(title)
                .author(authorDto)
                .build();

        when(service.getBookInfo(bookId)).thenReturn(responseDto);

        mockMvc.perform(get(baseUrl + "/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(bookId)))
                .andExpect(jsonPath("$.title", Matchers.is(title)))
                .andExpect(jsonPath("$.author.firstName", Matchers.is(firstName)))
                .andExpect(jsonPath("$.author.id", Matchers.is(authorId)))
                .andExpect(jsonPath("$.author.lastName", Matchers.is(lastName)));
    }

    @Test
    void whenGetBookInfo_givenBookIdNotFound_thenCorrect() throws Exception {
        when(service.getBookInfo(bookId)).thenThrow(new BookNotFoundException(bookId));

        mockMvc.perform(get(baseUrl + "/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("message", Matchers.is("Could not find book " + bookId)));
    }
}
