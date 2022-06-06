package com.mybooks.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybooks.api.exception.AuthorNotFoundException;
import com.mybooks.api.model.Author;
import com.mybooks.api.service.AuthorServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
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

import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {
    final private String baseUrl = "/book/author";
    final private String notFoundAuthorId = "51";
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    AuthorServiceImpl authorService;
    Author author1 = new Author("Author1_id", "Sam", "Simon");
    Author author2 = new Author("Author2_id", "Adi", "John");
    Author author3 = new Author("Author3_id", "Dodo", "Adel");

    @Test
    public void getAllAuthors_success() throws Exception {
        List<Author> authors = new ArrayList<>(Arrays.asList(author1, author2, author3));

        Mockito.when(authorService.getAllAuthors()).thenReturn(authors);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName", Matchers.is("Adi")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lastName", Matchers.is("John")));
    }

    @Test
    public void getAuthorById_success() throws Exception {
        Mockito.when(authorService.getAuthorById(author1.getId())).thenReturn(author1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl + "/" + author1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", Matchers.is(author1.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is(author1.getLastName())));
    }

    @Test
    public void getAuthorById_notFound() throws Exception {
        Mockito.when(authorService.getAuthorById(notFoundAuthorId)).thenThrow(new AuthorNotFoundException(notFoundAuthorId));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl + "/" + notFoundAuthorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AuthorNotFoundException))
                .andExpect(result ->
                        Assertions.assertEquals("Could not find author " + notFoundAuthorId,
                                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void createNewAuthor_success() throws Exception {
        Author author = getMockAuthor();
        Mockito.when(authorService.addAuthor(ArgumentMatchers.any(Author.class))).thenReturn(author);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(author));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", Matchers.is(author.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is(author.getLastName())));
    }

    @Test
    public void updateAuthor_success() throws Exception {
        Author updatedAuthor1 = Author.builder()
                .id(author1.getId())
                .firstName("Dark")
                .lastName("Tim")
                .build();
        Mockito.when(authorService.updateAuthor(ArgumentMatchers.any(Author.class), ArgumentMatchers.any(String.class))).thenReturn(updatedAuthor1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put(baseUrl + "/" + author1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedAuthor1));

        mockMvc.perform(mockRequest)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", Matchers.is(updatedAuthor1.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is(updatedAuthor1.getLastName())));
    }

    @Test
    public void updateAuthor_notFound() throws Exception {
        Author updatedAuthor = getMockAuthor();
        Mockito.when(authorService.updateAuthor(ArgumentMatchers.any(Author.class), ArgumentMatchers.any(String.class))).thenThrow(new AuthorNotFoundException(notFoundAuthorId));

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .put(baseUrl + "/" + notFoundAuthorId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedAuthor));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AuthorNotFoundException))
                .andExpect(result ->
                        Assertions.assertEquals("Could not find author 51",
                                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void deleteAuthorById_success() throws Exception {
        Mockito.doNothing().when(authorService).deleteAuthor(author1.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(baseUrl + "/" + author1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteAuthorById_notFound() throws Exception {
        Mockito.doThrow(new AuthorNotFoundException("51")).when(authorService).deleteAuthor("51");

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(baseUrl + "/51")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof AuthorNotFoundException))
                .andExpect(result ->
                        Assertions.assertEquals("Could not find author 51", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    private Author getMockAuthor() throws IOException {
        File file = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("Author.json")).getFile());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, Author.class);
    }
}
