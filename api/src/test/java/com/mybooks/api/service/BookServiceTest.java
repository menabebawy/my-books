package com.mybooks.api.service;

import com.mybooks.api.exception.BookNotFoundException;
import com.mybooks.api.model.Book;
import com.mybooks.api.model.MockBook;
import com.mybooks.api.reposiotry.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookServiceTest {
    @Mock
    BookRepository bookRepository;

    private BookService bookService;

    private final String notFoundBookId = "51";

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImp(bookRepository);
    }

    @AfterEach
    void destroyAll(){
        bookRepository.deleteAll();
    }

    @Test
    void getAllBooks_success() {
        Book book1 = new Book("Book1_id", "Title1", "author_id");
        Book book2 = new Book("Book2_id", "Title2", "author_id");
        Book book3 = new Book("Book3_id", "Title3", "author_id");
        List<Book> books = new ArrayList<>(Arrays.asList(book1, book2, book3));
        when(bookRepository.findAll()).thenReturn(books);
        assertEquals(bookService.getAllBooks().size(),  books.size());
    }

    @Test
    void getBookById_success() {
        Book book = MockBook.newBook;
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        assertDoesNotThrow(() -> bookService.getBookById(book.getId()));
        Book createdBook = bookService.getBookById(book.getId());
        assertEquals(createdBook.getId(), book.getId());
        assertEquals(createdBook.getTitle(), book.getTitle());
    }

    @Test
    void getBookById_notFound() {
        when(bookRepository.findById(notFoundBookId)).thenThrow(new BookNotFoundException(notFoundBookId));
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(notFoundBookId));
        verify(bookRepository, times(1)).findById(notFoundBookId);
    }

    @Test
    void createNewBook_success() {
        Book book = MockBook.newBook;
        when(bookRepository.save(book)).thenReturn(book);
        Book createdBook = bookService.createNewBook(book);
        assertEquals(createdBook.getTitle(), book.getTitle());
        assertEquals(createdBook.getId(), book.getId());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void updateBook_success() {
        Book book = MockBook.newBook;
        Book updatedBook = book;
        updatedBook.setTitle("New Title");
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        assertDoesNotThrow(() -> bookService.updateBook(updatedBook, book.getId()));
        assertEquals(bookService.updateBook(updatedBook, book.getId()).getTitle(), updatedBook.getTitle());
    }

    @Test
    void updateBook_notFound() {
        Book updatedBook = MockBook.newBook;
        updatedBook.setTitle("New Title");
        when(bookRepository.findById(updatedBook.getId())).thenThrow(new BookNotFoundException(updatedBook.getId()));
        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(updatedBook, updatedBook.getId()));
    }

    @Test
    public void deleteBook_notFound() {
        doThrow(BookNotFoundException.class).when(bookRepository).deleteById(notFoundBookId);
        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(notFoundBookId));
    }

    @Test
    public void deleteBook_success() {
        Book book = MockBook.newBook;
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).deleteById(book.getId());
        assertDoesNotThrow(() -> bookService.deleteBook(book.getId()));
    }
}
