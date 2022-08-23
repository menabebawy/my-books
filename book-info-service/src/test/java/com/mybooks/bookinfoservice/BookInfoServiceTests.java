package com.mybooks.bookinfoservice;

import com.mybooks.bookinfoservice.dto.AuthorDto;
import com.mybooks.bookinfoservice.dto.BookDto;
import com.mybooks.bookinfoservice.dto.BookInfoResponseDto;
import com.mybooks.bookinfoservice.exception.BookNotFoundException;
import com.mybooks.bookinfoservice.mapper.BookInfoMapper;
import com.mybooks.bookinfoservice.mapper.BookInfoMapperImpl;
import com.mybooks.bookinfoservice.model.Book;
import com.mybooks.bookinfoservice.repository.BookInfoRepository;
import com.mybooks.bookinfoservice.service.BookInfoServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BookInfoServiceTests {
    private final String bookId = "bookId";
    @Mock
    private BookInfoRepository repository;
    @Mock
    private RestTemplate restTemplate;
    private BookInfoMapper mapper;
    private BookInfoServiceImpl service;

    @BeforeEach
    void setup() {
        mapper = new BookInfoMapperImpl();
        service = new BookInfoServiceImpl(repository, mapper, restTemplate);
    }

    @AfterEach
    void destroyAll() {
        repository.deleteAll();
    }

    @Test
    void whenGetBookInfo_givenBookId_thenCorrect() {
        Book book = new Book();
        book.setId(bookId);
        String title = "title";
        book.setTitle(title);
        String authorId = "authorId";
        book.setAuthorId(authorId);

        BookDto bookDto = mapper.transferToBookDto(book);

        String firstName = "firstName";
        String lastName = "lastName";
        AuthorDto authorDto = AuthorDto.builder()
                .id(authorId)
                .firstName(firstName)
                .lastName(lastName)
                .build();

        when(repository.findById(bookId)).thenReturn(Optional.of(book));

        when(restTemplate.getForObject(
                "http://localhost:8082/book/author/" + bookDto.getAuthorId(), AuthorDto.class))
                .thenReturn(authorDto);

        BookInfoResponseDto responseDto = service.getBookInfo(bookId);

        assertEquals(responseDto.getId(), bookId);
        assertEquals(responseDto.getTitle(), title);
        assertEquals(responseDto.getAuthor().getId(), authorId);
        assertEquals(responseDto.getAuthor().getFirstName(), firstName);
        assertEquals(responseDto.getAuthor().getLastName(), lastName);
    }

    @Test
    void whenGetBookInfo_givenBookNotFound_thenCorrect() {
        when(repository.findById(bookId)).thenThrow(new BookNotFoundException(bookId));
        assertThrows(BookNotFoundException.class, () -> service.getBookInfo(bookId));
        verify(repository, Mockito.times(1)).findById(bookId);
    }
}
