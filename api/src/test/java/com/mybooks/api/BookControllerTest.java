package com.mybooks.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybooks.api.controller.BookController;
import com.mybooks.api.exception.BookNotFoundException;
import com.mybooks.api.model.Author;
import com.mybooks.api.model.Book;
import com.mybooks.api.reposiotry.BookRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    BookRepository bookRepository;

    Author author1 = new Author("Author1_id", "First", "Last");
    Book book1 = new Book("Book1_id", "Title1", author1.getId());

    Author author2 = new Author("Author2_id", "First", "Last");
    Book book2 = new Book("Book2_id", "Title2", author2.getId());

    Author author3 = new Author("Author3_id", "First", "Last");
    Book book3 = new Book("Book3_id", "Title3", author3.getId());

    final private String baseUrl = "/book";

    @Test
    public void getAllBook_success() throws Exception {
        List<Book> books = new ArrayList<>(Arrays.asList(book1, book2, book3));

        Mockito.when(bookRepository.findAll()).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].title", Matchers.is("Title3")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].authorId", Matchers.is("Author3_id")));
    }

    @Test
    public void getBookById_success() throws Exception {
        Mockito.when(bookRepository.findById(book1.getId())).thenReturn(Optional.ofNullable(book1));

        mockMvc.perform(MockMvcRequestBuilders
                .get(baseUrl + "/Book1_id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("Title1")));
    }

    @Test
    public void createNewBook_success() throws Exception {
        Author author = Author.builder()
                .id("Author_id")
                .firstName("Jac")
                .lastName("Daniel")
                .build();
        Book book = Book.builder()
                .id("Book_id")
                .title("My New Book")
                .authorId(author.getId())
                .build();

        Mockito.when(bookRepository.save(book)).thenReturn(book);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(book));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(book.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorId", Matchers.is(book.getAuthorId())));
    }

    @Test
    public void updateBook_success() throws Exception {
        Book updatedBook = Book.builder()
                .id("Book1_id")
                .title("Riche Father")
                .authorId("Author_id")
                .build();

        Mockito.when(bookRepository.save(book1)).thenReturn(book1);
        Mockito.when(bookRepository.findById(book1.getId())).thenReturn(Optional.ofNullable(book1));
        Mockito.when(bookRepository.save(updatedBook)).thenReturn(updatedBook);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put(baseUrl + "/Book1_id")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedBook));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(updatedBook.getTitle())));
    }

    @Test
    public void updateBook_notFound() throws Exception {
        Book updatedBook = Book.builder()
                .id("51")
                .title("Riche Father")
                .authorId("Author_id")
                .build();

        Mockito.when(bookRepository.findById(updatedBook.getId())).thenThrow(new BookNotFoundException("51"));

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.put(baseUrl + "/51")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedBook));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof BookNotFoundException))
                .andExpect(result ->
                        assertEquals("Could not find book 51", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void deleteBookById_success() throws Exception {
        Mockito.when(bookRepository.findById(book2.getId())).thenReturn(Optional.of(book2));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(baseUrl + "/Book2_id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteBookById_notFound() throws Exception {
        Mockito.when(bookRepository.findById("51")).thenThrow(new BookNotFoundException("51"));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(baseUrl + "/51")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof BookNotFoundException))
                .andExpect(result ->
                        assertEquals("Could not find book 51", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
}
