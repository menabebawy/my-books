package com.mybooks.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybooks.api.dto.BookDTO;
import com.mybooks.api.exception.BookNotFoundException;
import com.mybooks.api.mapper.BookMapper;
import com.mybooks.api.model.Book;
import com.mybooks.api.service.BookService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WithMockUser
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class BookControllerTest {
    private final BookDTO book1 = BookDTO.builder().id("Book1_id").title("Title1").authorId("Author1_id").build();
    private final BookDTO book2 = BookDTO.builder().id("Book2_id").title("Title2").authorId("Author2_id").build();
    private final BookDTO book3 = BookDTO.builder().id("Book3_id").title("Title3").authorId("Author3_id").build();

    private final String baseUrl = "/book";
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    BookService bookService;
    @Autowired
    BookMapper bookMapper;

    @Test
    void getAllBook_success() throws Exception {
        List<BookDTO> books = new ArrayList<>(Arrays.asList(book1, book2, book3));

        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[2].title", Matchers.is("Title3")))
                .andExpect(jsonPath("$[2].authorId", Matchers.is("Author3_id")));
    }

    @Test
    void getBookById_success() throws Exception {
        when(bookService.getBookById(book1.getId())).thenReturn(book1);

        mockMvc.perform(get(baseUrl + "/" + book1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.title", Matchers.is(book1.getTitle())));
    }

    @Test
    void whenGetBookRequestByNotFoundId_thenCorrectResponse() throws Exception {
        when(bookService.getBookById(book1.getId())).thenThrow(new BookNotFoundException(book1.getId()));

        mockMvc.perform(get(baseUrl + "/" + book1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("message", Matchers.is("Could not find book " + book1.getId())));
    }

    @Test
    void addNewBook_success() throws Exception {
        Book newBook = getMockBook();

        when(bookService.addNewBook(ArgumentMatchers.any(BookDTO.class))).thenReturn(bookMapper.transformToBookDTO(newBook));

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(newBook)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.title", Matchers.is(newBook.getTitle())))
                .andExpect(jsonPath("$.authorId", Matchers.is(newBook.getAuthorId())));
    }

    @Test
    void whenPostRequestToBookAndInValidBookTitle_thenCorrectResponse() throws Exception {
        Book newBook = getMockBook();
        newBook.setTitle(null);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(newBook)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", Matchers.is("title is required")));
    }

    @Test
    void whenPostRequestToBookAndInValidBookAuthorId_thenCorrectResponse() throws Exception {
        Book newBook = getMockBook();
        newBook.setAuthorId(null);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(newBook)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", Matchers.is("authorId is required")));
    }

    @Test
    void updateBook_success() throws Exception {
        BookDTO updatedBook = BookDTO.builder()
                .id(getMockBook().getId())
                .title("New Title")
                .authorId(getMockBook().getAuthorId())
                .build();

        when(bookService.updateBook(ArgumentMatchers.any(BookDTO.class), ArgumentMatchers.any(String.class))).thenReturn(updatedBook);

        mockMvc.perform(put(baseUrl + "/" + updatedBook.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.title", Matchers.is(updatedBook.getTitle())));
    }

    @Test
    void whenPutRequestToBookAndInvalidBookTitle_thenCorrectResponse() throws Exception {
        BookDTO updatedBook = BookDTO.builder()
                .id(getMockBook().getId())
                .title("")
                .authorId(getMockBook().getAuthorId())
                .build();

        mockMvc.perform(put(baseUrl + "/" + updatedBook.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(updatedBook)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("message", Matchers.is("title is required")));
    }

    @Test
    void whenPutRequestToBookAndInvalidBookAuthorId_thenCorrectResponse() throws Exception {
        BookDTO updatedBook = BookDTO.builder()
                .id(getMockBook().getId())
                .title(getMockBook().getTitle())
                .authorId(null)
                .build();

        mockMvc.perform(put(baseUrl + "/" + updatedBook.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(updatedBook)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("message", Matchers.is("authorId is required")));
    }

    @Test
    void updateBook_notFound() throws Exception {
        when(bookService.updateBook(ArgumentMatchers.any(BookDTO.class), ArgumentMatchers.any(String.class))).thenThrow(new BookNotFoundException("51"));

        mockMvc.perform(put(baseUrl + "/" + getMockBook().getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(getMockBook())))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof BookNotFoundException))
                .andExpect(result ->
                        assertEquals("Could not find book 51", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void deleteBookById_success() throws Exception {
        doNothing().when(bookService).deleteBook(book1.getId());

        mockMvc.perform(delete(baseUrl + "/" + book1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteBookById_notFound() throws Exception {
        doThrow(new BookNotFoundException("51")).when(bookService).deleteBook("51");

        mockMvc.perform(delete(baseUrl + "/51")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof BookNotFoundException))
                .andExpect(result ->
                        assertEquals("Could not find book 51", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    private Book getMockBook() throws IOException {
        File file = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("Book.json")).getFile());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, Book.class);
    }
}
