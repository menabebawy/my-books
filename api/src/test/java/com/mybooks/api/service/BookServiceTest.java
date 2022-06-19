package com.mybooks.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybooks.api.dto.BookDTO;
import com.mybooks.api.exception.BookNotFoundException;
import com.mybooks.api.mapper.BookMapper;
import com.mybooks.api.mapper.BookMapperImpl;
import com.mybooks.api.model.Book;
import com.mybooks.api.repository.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookServiceTest {
    private final String notFoundBookId = "51";
    @Mock
    BookRepository bookRepository;

    private BookMapper bookMapper;
    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookMapper = new BookMapperImpl();
        bookService = new BookServiceImpl(bookRepository, bookMapper);
    }

    @AfterEach
    void destroyAll() {
        bookRepository.deleteAll();
    }

    @Test
    void fetchAllBooks_success() {
        BookDTO book1 = BookDTO.builder().id("Book1_id").title("Title1").authorId("author_id").build();
        BookDTO book2 = BookDTO.builder().id("Book2_id").title("Title2").authorId("author_id").build();
        BookDTO book3 = BookDTO.builder().id("Book3_id").title("Title3").authorId("author_id").build();
        List<BookDTO> createdBooks = new ArrayList<>(Arrays.asList(book1, book2, book3));
        List<Book> createdBooksList = createdBooks.stream().map(bookMapper::transformToBook).collect(Collectors.toList());
        when(bookRepository.findAll()).thenReturn(createdBooksList);
        List<BookDTO> returnedBooksList = bookService.fetchAll();
        assertEquals(returnedBooksList.size(), createdBooksList.size());
    }

    @Test
    void fetchBookById_success() throws IOException {
        Book book = getMockBook();
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        assertDoesNotThrow(() -> bookService.fetchById(book.getId()));
        BookDTO createdBook = bookService.fetchById(book.getId());
        assertEquals(createdBook.getId(), book.getId());
        assertEquals(createdBook.getTitle(), book.getTitle());
    }

    @Test
    void getBookById_notFound() {
        when(bookRepository.findById(notFoundBookId)).thenThrow(new BookNotFoundException(notFoundBookId));
        assertThrows(BookNotFoundException.class, () -> bookService.fetchById(notFoundBookId));
        verify(bookRepository, Mockito.times(1)).findById(notFoundBookId);
    }

    @Test
    void createNewBook_success() throws IOException {
        Book book = getMockBook();
        when(bookRepository.save(book)).thenReturn(book);
        BookDTO createdBook = bookService.save(bookMapper.transformToBookDTO(book));
        assertEquals(createdBook.getTitle(), book.getTitle());
        assertEquals(createdBook.getId(), book.getId());
        verify(bookRepository, Mockito.times(1)).save(book);
    }

    @Test
    void updateBook_success() throws IOException {
        Book book = getMockBook();
        BookDTO updatedBookDTO = BookDTO.builder()
                .id(getMockBook().getId())
                .title("New Title")
                .authorId(getMockBook().getAuthorId())
                .build();
        Book updatedBook = bookMapper.transformToBook(updatedBookDTO);
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(updatedBook);
        assertDoesNotThrow(() -> bookService.update(updatedBookDTO, book.getId()));
        assertEquals(bookService.update(updatedBookDTO, book.getId()).getTitle(), updatedBookDTO.getTitle());
    }

    @Test
    void updateBook_notFound() throws IOException {
        BookDTO updatedBook = BookDTO.builder()
                .id(getMockBook().getId())
                .title("New Title")
                .authorId(getMockBook().getAuthorId()).build();
        when(bookRepository.findById(updatedBook.getId())).thenThrow(new BookNotFoundException(updatedBook.getId()));
        assertThrows(BookNotFoundException.class, () -> bookService.update(updatedBook, updatedBook.getId()));
    }

    @Test
    public void deleteBook_notFound() {
        doThrow(BookNotFoundException.class).when(bookRepository).deleteById(notFoundBookId);
        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(notFoundBookId));
    }

    @Test
    public void deleteBook_success() throws IOException {
        Book book = getMockBook();
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).deleteById(book.getId());
        assertDoesNotThrow(() -> bookService.deleteBook(book.getId()));
    }

    private Book getMockBook() throws IOException {
        File file = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("Book.json")).getFile());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, Book.class);
    }
}
