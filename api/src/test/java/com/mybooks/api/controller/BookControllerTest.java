package com.mybooks.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybooks.api.exception.BookNotFoundException;
import com.mybooks.api.model.Book;
import com.mybooks.api.service.BookService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;

@WebMvcTest(BookController.class)
public class BookControllerTest {
    private final Book book1 = new Book("Book1_id", "Title1", "Author1_id");
    private final Book book2 = new Book("Book2_id", "Title2", "Author2_id");
    private final Book book3 = new Book("Book3_id", "Title3", "Author3_id");
    final private String baseUrl = "/book";
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    BookService bookService;

    @Test
    public void getAllBook_success() throws Exception {
        List<Book> books = new ArrayList<>(Arrays.asList(book1, book2, book3));

        Mockito.when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].title", Matchers.is("Title3")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].authorId", Matchers.is("Author3_id")));
    }

    @Test
    public void getBookById_success() throws Exception {
        Mockito.when(bookService.getBookById(book1.getId())).thenReturn(book1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl + "/" + book1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(book1.getTitle())));
    }

//    @Test
//    public void createNewBook_success() throws Exception {
//        Book newBook = getMockBook();
//
//        Mockito.when(bookService.createNewBook(ArgumentMatchers.any(Book.class))).thenReturn(newBook);
//
//        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post(baseUrl)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(this.mapper.writeValueAsString(newBook));
//
//        mockMvc.perform(mockHttpServletRequestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(newBook.getTitle())))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.authorId", Matchers.is(newBook.getAuthorId())));
//    }

//    @Test
//    public void updateBook_success() throws Exception {
//        Book updatedBook = getMockBook();
//        updatedBook.setTitle("New Title");
//
//        Mockito.when(bookService.updateBook(ArgumentMatchers.any(Book.class), ArgumentMatchers.any(String.class))).thenReturn(updatedBook);
//
//        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put(baseUrl + "/" + updatedBook.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(this.mapper.writeValueAsString(updatedBook));
//
//        mockMvc.perform(mockRequest)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(updatedBook.getTitle())));
//    }

//    @Test
//    public void updateBook_notFound() throws Exception {
//        Mockito.when(bookService.updateBook(ArgumentMatchers.any(Book.class), ArgumentMatchers.any(String.class))).thenThrow(new BookNotFoundException("51"));
//
//        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
//                .put(baseUrl + "/" + getMockBook().getId())
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(this.mapper.writeValueAsString(getMockBook()));
//
//        mockMvc.perform(mockHttpServletRequestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isNotFound())
//                .andExpect(result ->
//                        assertTrue(result.getResolvedException() instanceof BookNotFoundException))
//                .andExpect(result ->
//                        Assertions.assertEquals("Could not find book 51", Objects.requireNonNull(result.getResolvedException()).getMessage()));
//    }

    @Test
    public void deleteBookById_success() throws Exception {
        Mockito.doNothing().when(bookService).deleteBook(book1.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(baseUrl + "/" + book1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteBookById_notFound() throws Exception {
        doThrow(new BookNotFoundException("51")).when(bookService).deleteBook("51");

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(baseUrl + "/51")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof BookNotFoundException))
                .andExpect(result ->
                        Assertions.assertEquals("Could not find book 51", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    private Book getMockBook() throws IOException {
        File file = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("Book.json")).getFile());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, Book.class);
    }
}
