package com.mybooks.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybooks.api.dto.BookDTO;
import com.mybooks.api.exception.BookNotFoundException;
import com.mybooks.api.mapper.BookMapper;
import com.mybooks.api.mapper.BookMapperImpl;
import com.mybooks.api.model.Book;
import com.mybooks.api.service.BookService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {
    private final BookDTO book1 = BookDTO.builder().id("Book1_id").title("Title1").authorId("Author1_id").build();
    private final BookDTO book2 = BookDTO.builder().id("Book2_id").title("Title2").authorId("Author2_id").build();
    private final BookDTO book3 = BookDTO.builder().id("Book3_id").title("Title3").authorId("Author3_id").build();

    final private String baseUrl = "/book";
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    BookService bookService;
    private BookMapper bookMapper;

    @BeforeEach
    void setUp() {
        bookMapper = new BookMapperImpl();
    }

    @Test
    public void getAllBook_success() throws Exception {
        List<BookDTO> books = new ArrayList<>(Arrays.asList(book1, book2, book3));

        when(bookService.fetchAll()).thenReturn(books);

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
        when(bookService.fetchById(book1.getId())).thenReturn(book1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl + "/" + book1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(book1.getTitle())));
    }

    @Test
    public void whenGetBookRequestByNotFoundId_thenCorrectResponse() throws Exception {
        when(bookService.fetchById(book1.getId())).thenThrow(new BookNotFoundException(book1.getId()));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl + "/" + book1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("message", Matchers.is("Could not find book " + book1.getId())));
    }

    @Test
    public void addNewBook_success() throws Exception {
        Book newBook = getMockBook();

        when(bookService.save(ArgumentMatchers.any(BookDTO.class))).thenReturn(bookMapper.transformToBookDTO(newBook));

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(newBook));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(newBook.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorId", Matchers.is(newBook.getAuthorId())));
    }

    @Test
    public void whenPostRequestToBookAndInValidBookTitle_thenCorrectResponse() throws Exception {
        Book newBook = getMockBook();
        newBook.setTitle(null);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(newBook));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("message", Matchers.is("title is required")));
    }

    @Test
    public void whenPostRequestToBookAndInValidBookAuthorId_thenCorrectResponse() throws Exception {
        Book newBook = getMockBook();
        newBook.setAuthorId(null);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(newBook));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("message", Matchers.is("authorId is required")));
    }

    @Test
    public void updateBook_success() throws Exception {
        BookDTO updatedBook = BookDTO.builder()
                .id(getMockBook().getId())
                .title("New Title")
                .authorId(getMockBook().getAuthorId())
                .build();

        when(bookService.update(ArgumentMatchers.any(BookDTO.class), ArgumentMatchers.any(String.class))).thenReturn(updatedBook);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put(baseUrl + "/" + updatedBook.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedBook));

        mockMvc.perform(mockRequest)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(updatedBook.getTitle())));
    }

    @Test
    public void whenPutRequestToBookAndInvalidBookTitle_thenCorrectResponse() throws Exception {
        BookDTO updatedBook = BookDTO.builder()
                .id(getMockBook().getId())
                .title("")
                .authorId(getMockBook().getAuthorId())
                .build();

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put(baseUrl + "/" + updatedBook.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedBook));

        mockMvc.perform(mockRequest)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("message", Matchers.is("title is required")));
    }

    @Test
    public void whenPutRequestToBookAndInvalidBookAuthorId_thenCorrectResponse() throws Exception {
        BookDTO updatedBook = BookDTO.builder()
                .id(getMockBook().getId())
                .title(getMockBook().getTitle())
                .authorId(null)
                .build();

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put(baseUrl + "/" + updatedBook.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedBook));

        mockMvc.perform(mockRequest)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("message", Matchers.is("authorId is required")));
    }

    @Test
    public void updateBook_notFound() throws Exception {
        when(bookService.update(ArgumentMatchers.any(BookDTO.class), ArgumentMatchers.any(String.class))).thenThrow(new BookNotFoundException("51"));

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .put(baseUrl + "/" + getMockBook().getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(getMockBook()));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
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
                .andExpect(MockMvcResultMatchers.status().isNoContent());
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
                        assertEquals("Could not find book 51", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    private Book getMockBook() throws IOException {
        File file = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("Book.json")).getFile());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, Book.class);
    }
}
