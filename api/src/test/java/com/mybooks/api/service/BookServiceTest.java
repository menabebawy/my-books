package com.mybooks.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybooks.api.model.Book;
import com.mybooks.api.repository.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@SpringBootTest
public class BookServiceTest {
    private final String notFoundBookId = "51";
    @Mock
    BookRepository bookRepository;
    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl(bookRepository);
    }

    @AfterEach
    void destroyAll() {
        bookRepository.deleteAll();
    }

//    @Test
//    void getAllBooks_success() {
//        Book book1 = new Book("Book1_id", "Title1", "author_id");
//        Book book2 = new Book("Book2_id", "Title2", "author_id");
//        Book book3 = new Book("Book3_id", "Title3", "author_id");
//        List<Book> books = new ArrayList<>(Arrays.asList(book1, book2, book3));
//        Mockito.when(bookRepository.findAll()).thenReturn(books);
//        assertEquals(bookService.getAllBooks().size(), books.size());
//    }

//    @Test
//    void getBookById_success() throws IOException {
//        Book book = getMockBook();
//        Mockito.when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
//        Assertions.assertDoesNotThrow(() -> bookService.getBookById(book.getId()));
//        Book createdBook = bookService.getBookById(book.getId());
//        assertEquals(createdBook.getId(), book.getId());
//        assertEquals(createdBook.getTitle(), book.getTitle());
//    }

//    @Test
//    void getBookById_notFound() {
//        Mockito.when(bookRepository.findById(notFoundBookId)).thenThrow(new BookNotFoundException(notFoundBookId));
//        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(notFoundBookId));
//        Mockito.verify(bookRepository, Mockito.times(1)).findById(notFoundBookId);
//    }

//    @Test
//    void createNewBook_success() throws IOException {
//        Book book = getMockBook();
//        Mockito.when(bookRepository.save(book)).thenReturn(book);
//        Book createdBook = bookService.createNewBook(book);
//        assertEquals(createdBook.getTitle(), book.getTitle());
//        assertEquals(createdBook.getId(), book.getId());
//        Mockito.verify(bookRepository, Mockito.times(1)).save(book);
//    }

//    @Test
//    void updateBook_success() throws IOException {
//        Book book = getMockBook();
//        Book updatedBook = getMockBook();
//        updatedBook.setTitle("New Title");
//        Mockito.when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
//        Assertions.assertDoesNotThrow(() -> bookService.updateBook(updatedBook, book.getId()));
//        assertEquals(bookService.updateBook(updatedBook, book.getId()).getTitle(), updatedBook.getTitle());
//    }

//    @Test
//    void updateBook_notFound() throws IOException {
//        Book updatedBook = getMockBook();
//        updatedBook.setTitle("New Title");
//        Mockito.when(bookRepository.findById(updatedBook.getId())).thenThrow(new BookNotFoundException(updatedBook.getId()));
//        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(updatedBook, updatedBook.getId()));
//    }

//    @Test
//    public void deleteBook_notFound() {
//        doThrow(BookNotFoundException.class).when(bookRepository).deleteById(notFoundBookId);
//        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(notFoundBookId));
//    }

//    @Test
//    public void deleteBook_success() throws IOException {
//        Book book = getMockBook();
//        Mockito.when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
//        Mockito.doNothing().when(bookRepository).deleteById(book.getId());
//        assertDoesNotThrow(() -> bookService.deleteBook(book.getId()));
//    }

    private Book getMockBook() throws IOException {
        File file = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("Book.json")).getFile());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, Book.class);
    }
}
