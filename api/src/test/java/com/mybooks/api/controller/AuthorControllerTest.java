package com.mybooks.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybooks.api.exception.AuthorNotFoundException;
import com.mybooks.api.model.Author;
import com.mybooks.api.reposiotry.AuthorRepository;
import com.mybooks.api.service.AuthorService;
import com.mybooks.api.service.AuthorServiceImp;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AuthorControllerTest {
    MockMvc mockMvc;

    ObjectMapper mapper;

    @Mock
    AuthorService authorService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Mock
    AuthorRepository authorRepository;

    Author author1 = new Author("Author1_id", "Sam", "Simon");
    Author author2 = new Author("Author2_id", "Adi", "John");
    Author author3 = new Author("Author3_id", "Dodo", "Adel");

    final private String baseUrl = "/book/author";

    @BeforeEach
    void setUp() {
        this.mapper = new ObjectMapper();
        this.authorService = Mockito.spy(new AuthorServiceImp(authorRepository));
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void getAllAuthors_success() throws Exception {
        List<Author> authors = new ArrayList<>(Arrays.asList(author1, author2, author3));

        Mockito.when(authorService.getAllAuthors()).thenReturn(authors);

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
        Mockito.when(authorService.getAuthorById(author1.getId())).thenReturn(author1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl + "/Author1_id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", Matchers.is("Sam")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is("Simon")));
    }

    @Test
    public void getAuthorById_notFound() throws Exception {
        Mockito.doThrow(new AuthorNotFoundException("51")).when(authorService.getAuthorById("51"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl + "/51")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof AuthorNotFoundException))
                .andExpect(result ->
                        assertEquals("Could not find author 51", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void createNewAuthor_success() throws Exception {
        Author author = Author.builder()
                .id("Author_id")
                .firstName("Jac")
                .lastName("Daniel")
                .build();

        Mockito.when(authorService.createNewAuthor(author)).thenReturn(author);

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
                .id(author1.getId())
                .firstName("Dark")
                .lastName("Tim")
                .build();
        Mockito.when(authorService.updateAuthor(updatedAuthor1, author1.getId())).thenReturn(updatedAuthor1);

        System.out.println(authorService.getAllAuthors());

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put(baseUrl + "/"+ author1.getId())
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
                .id("100")
                .firstName("Riche")
                .lastName("Tim")
                .build();

        Mockito.when(authorService.updateAuthor(updatedAuthor, "100")).thenThrow(new AuthorNotFoundException("100"));

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.put(baseUrl + "/100")
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
        Mockito.doNothing().when(authorService).deleteAuthor(author1.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(baseUrl + "/" + author1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteAuthorById_notFound() throws Exception {
        Mockito.doThrow(new AuthorNotFoundException("51")).when(authorService).deleteAuthor("51");

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
