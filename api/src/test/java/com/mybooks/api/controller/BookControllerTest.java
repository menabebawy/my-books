package com.mybooks.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybooks.api.exception.BookNotFoundException;
import com.mybooks.api.model.Book;
import com.mybooks.api.model.MockBook;
import com.mybooks.api.service.BookService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookService bookService;

    private Book book1 = new Book("Book1_id", "Title1", "Author1_id");
    private Book book2 = new Book("Book2_id", "Title2", "Author2_id");
    private Book book3 = new Book("Book3_id", "Title3", "Author3_id");

    final private String baseUrl = "/book";

    @Test
    public void getAllBook_success() throws Exception {
        List<Book> books = new ArrayList<>(Arrays.asList(book1, book2, book3));

       when(bookService.getAllBooks()).thenReturn(books);

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
        when(bookService.getBookById(book1.getId())).thenReturn(book1);

        mockMvc.perform(MockMvcRequestBuilders
                .get(baseUrl + "/" + book1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(book1.getTitle())));
    }

    @Test
    public void createNewBook_success() throws Exception {
        Book newBook = MockBook.newBook;

        when(bookService.createNewBook(any(Book.class))).thenReturn(newBook);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(newBook));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(newBook.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorId", Matchers.is(newBook.getAuthorId())));
    }

    @Test
    public void updateBook_success() throws Exception {
        Book updatedBook = MockBook.newBook;
        updatedBook.setTitle("New Title");

        when(bookService.updateBook(any(Book.class), any(String.class))).thenReturn(updatedBook);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put(baseUrl + "/" + updatedBook.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedBook));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(updatedBook.getTitle())));
    }

    @Test
    public void updateBook_notFound() throws Exception {
        when(bookService.updateBook(any(Book.class), any(String.class))).thenThrow(new BookNotFoundException("51"));

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .put(baseUrl + "/" + MockBook.newBook.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(MockBook.newBook));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof BookNotFoundException))
                .andExpect(result ->
                        assertEquals("Could not find book 51", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void deleteBookById_success() throws Exception {
        doNothing().when(bookService).deleteBook(book1.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(baseUrl + "/" + book1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteBookById_notFound() throws Exception {
        doThrow(new BookNotFoundException("51")).when(bookService).deleteBook("51");

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
