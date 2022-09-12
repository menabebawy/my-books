package com.mybooks.resourceservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybooks.resourceservice.dto.BookDTO;
import com.mybooks.resourceservice.dto.BookRequestDTO;
import com.mybooks.resourceservice.exception.BookNotFoundException;
import com.mybooks.resourceservice.mapper.BookMapper;
import com.mybooks.resourceservice.mapper.BookMapperImpl;
import com.mybooks.resourceservice.model.Book;
import com.mybooks.resourceservice.repository.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
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
class BookServiceTest {
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
        List<BookDTO> returnedBooksList = bookService.getAllBooks();
        assertEquals(returnedBooksList.size(), createdBooksList.size());
    }

    @Test
    void fetchBookById_success() throws IOException {
        Book book = getMockBook();
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        assertDoesNotThrow(() -> bookService.getBookById(book.getId()));
        BookDTO createdBook = bookService.getBookById(book.getId());
        assertEquals(createdBook.getId(), book.getId());
        assertEquals(createdBook.getTitle(), book.getTitle());
    }

    @Test
    void getBookById_notFound() {
        when(bookRepository.findById(notFoundBookId)).thenThrow(new BookNotFoundException(notFoundBookId));
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(notFoundBookId));
        verify(bookRepository, Mockito.times(1)).findById(notFoundBookId);
    }

    @Test
    void createNewBook_success() throws IOException {
        Book book = getMockBook();
        when(bookRepository.save(ArgumentMatchers.any(Book.class))).thenReturn(book);
        BookRequestDTO bookRequestDTO = BookRequestDTO.builder()
                .title(book.getTitle())
                .authorId(book.getAuthorId())
                .build();
        BookDTO createdBook = bookService.addNewBook(bookRequestDTO);
        assertEquals(createdBook.getTitle(), book.getTitle());
        assertEquals(createdBook.getId(), book.getId());
        verify(bookRepository, Mockito.times(1)).save(ArgumentMatchers.any(Book.class));
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
        assertDoesNotThrow(() -> bookService.updateBook(updatedBookDTO, book.getId()));
        assertEquals(bookService.updateBook(updatedBookDTO, book.getId()).getTitle(), updatedBookDTO.getTitle());
    }

    @Test
    void updateBook_notFound() throws IOException {
        BookDTO updatedBook = BookDTO.builder()
                .id(getMockBook().getId())
                .title("New Title")
                .authorId(getMockBook().getAuthorId()).build();
        when(bookRepository.findById(updatedBook.getId())).thenThrow(new BookNotFoundException(updatedBook.getId()));
        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(updatedBook, updatedBook.getId()));
    }

    @Test
    void deleteBook_notFound() {
        doThrow(BookNotFoundException.class).when(bookRepository).deleteById(notFoundBookId);
        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(notFoundBookId));
    }

    @Test
    void deleteBook_success() throws IOException {
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
