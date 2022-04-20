package com.mybooks.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybooks.api.controller.AuthorController;
import com.mybooks.api.exception.AuthorNotFoundException;
import com.mybooks.api.model.Author;
import com.mybooks.api.reposiotry.AuthorRepository;
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

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    AuthorRepository authorRepository;

    Author author1 = new Author("Author1_id", "Sam", "Simon");
    Author author2 = new Author("Author2_id", "Adi", "John");
    Author author3 = new Author("Author3_id", "Dodo", "Adel");

    final private String baseUrl = "/book/author";

    @Test
    public void getAllAuthors_success() throws Exception {
        List<Author> authors = new ArrayList<>(Arrays.asList(author1, author2, author3));

        Mockito.when(authorRepository.findAll()).thenReturn(authors);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName", Matchers.is("Adi")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lastName", Matchers.is("John")));
    }

    @Test
    public void getAuthorById_success() throws Exception {
        Mockito.when(authorRepository.findById(author1.getId())).thenReturn(Optional.ofNullable(author1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl + "/Author1_id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", Matchers.is("Sam")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is("Simon")));
    }

    @Test
    public void createNewAuthor_success() throws Exception {
        Author author = Author.builder()
                .id("Author_id")
                .firstName("Jac")
                .lastName("Daniel")
                .build();

        Mockito.when(authorRepository.save(author)).thenReturn(author);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(author));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", Matchers.is(author.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is(author.getLastName())));
    }

    @Test
    public void updateAuthor_success() throws Exception {
        Author updatedAuthor1 = Author.builder()
                .id("Author1_id")
                .firstName("Dark")
                .lastName("Tim")
                .build();

        Mockito.when(authorRepository.save(author1)).thenReturn(author1);
        Mockito.when(authorRepository.findById(author1.getId())).thenReturn(Optional.ofNullable(author1));
        Mockito.when(authorRepository.save(updatedAuthor1)).thenReturn(updatedAuthor1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put(baseUrl + "/Author1_id")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedAuthor1));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", Matchers.is(updatedAuthor1.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is(updatedAuthor1.getLastName())));
    }

    @Test
    public void updateAuthor_notFound() throws Exception {
        Author updatedAuthor = Author.builder()
                .id("51")
                .firstName("Riche")
                .lastName("Tim")
                .build();

        Mockito.when(authorRepository.findById(updatedAuthor.getId())).thenThrow(new AuthorNotFoundException("51"));

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.put(baseUrl + "/51")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedAuthor));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof AuthorNotFoundException))
                .andExpect(result ->
                        assertEquals("Could not find author 51", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void deleteAuthorById_success() throws Exception {
        Mockito.when(authorRepository.findById(author2.getId())).thenReturn(Optional.of(author2));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(baseUrl + "/Author2_id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteAuthorById_notFound() throws Exception {
        Mockito.when(authorRepository.findById("51")).thenThrow(new AuthorNotFoundException("51"));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(baseUrl + "/51")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof AuthorNotFoundException))
                .andExpect(result ->
                        assertEquals("Could not find author 51", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
}
