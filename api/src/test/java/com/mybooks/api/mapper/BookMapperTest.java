package com.mybooks.api.mapper;

import com.mybooks.api.dto.BookDTO;
import com.mybooks.api.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookMapperTest {
    private BookMapper bookMapper;

    @BeforeEach
    void setup() {
        bookMapper = new BookMapperImpl();
    }

    @Test
    void givenBookEntityToBookDTO_whenMaps_ThenCorrect() {
        Book book = new Book();
        book.setId("book_id");
        book.setTitle("book_title");
        book.setAuthorId("author_id");
        BookDTO bookDTO = bookMapper.transformToBookDTO(book);
        assertEquals(book.getId(), bookDTO.getId());
        assertEquals(book.getTitle(), bookDTO.getTitle());
        assertEquals(book.getAuthorId(), bookDTO.getAuthorId());
    }

    @Test
    void givenBookDTOToBookEntity_whenMaps_ThenCorrect() {
        BookDTO bookDTO = BookDTO.builder()
                .id("book_id")
                .title("book_title")
                .authorId("author_id")
                .build();
        Book book = bookMapper.transformToBook(bookDTO);
        assertEquals(book.getId(), bookDTO.getId());
        assertEquals(book.getTitle(), bookDTO.getTitle());
        assertEquals(book.getAuthorId(), bookDTO.getAuthorId());
    }
}
