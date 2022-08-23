package com.mybooks.bookinfoservice;

import com.mybooks.bookinfoservice.dto.BookDto;
import com.mybooks.bookinfoservice.mapper.BookInfoMapper;
import com.mybooks.bookinfoservice.mapper.BookInfoMapperImpl;
import com.mybooks.bookinfoservice.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookInfoMapperTests {
    private BookInfoMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new BookInfoMapperImpl();
    }

    @Test
    void whenTransferToBookDto_givenBookEntity_thenCorrect() {
        String id = "id";
        String title = "title";
        String authorId = "authorId";

        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setAuthorId(authorId);

        BookDto dto = mapper.transferToBookDto(book);

        assertEquals(dto.getId(), id);
        assertEquals(dto.getTitle(), title);
        assertEquals(dto.getAuthorId(), authorId);
    }
}
