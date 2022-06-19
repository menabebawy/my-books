package com.mybooks.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybooks.api.dto.AuthorDTO;
import com.mybooks.api.exception.AuthorNotFoundException;
import com.mybooks.api.mapper.AuthorMapper;
import com.mybooks.api.mapper.AuthorMapperImpl;
import com.mybooks.api.model.Author;
import com.mybooks.api.service.AuthorServiceImpl;
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
    AuthorDTO author1 = AuthorDTO.builder().id("Author1_id").firstName("Sam").lastName("Simon").build();
    AuthorDTO author2 = AuthorDTO.builder().id("Author2_id").firstName("Adi").lastName("John").build();
    AuthorDTO author3 = AuthorDTO.builder().id("Author3_id").firstName("Dodo").lastName("Adel").build();
    private AuthorMapper authorMapper;

    @BeforeEach
    void setUp() {
        authorMapper = new AuthorMapperImpl();
    }

    @Test
    public void getAllAuthors_success() throws Exception {
        List<AuthorDTO> authors = new ArrayList<>(Arrays.asList(author1, author2, author3));

        when(authorService.fetchAll()).thenReturn(authors);

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
        when(authorService.fetchById(author1.getId())).thenReturn(author1);

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
        when(authorService.fetchById(notFoundAuthorId)).thenThrow(new AuthorNotFoundException(notFoundAuthorId));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl + "/" + notFoundAuthorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AuthorNotFoundException))
                .andExpect(result ->
                        assertEquals("Could not find author " + notFoundAuthorId,
                                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void createNewAuthor_success() throws Exception {
        Author author = getMockAuthor();
        when(authorService.save(ArgumentMatchers.any(AuthorDTO.class))).thenReturn(authorMapper.transformToAuthorDTO(author));

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(author));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", Matchers.is(author.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is(author.getLastName())));
    }

    @Test
    public void updateAuthor_success() throws Exception {
        AuthorDTO updatedAuthor1 = AuthorDTO.builder()
                .id(author1.getId())
                .firstName("Dark")
                .lastName("Tim")
                .build();
        when(authorService.update(ArgumentMatchers.any(AuthorDTO.class), ArgumentMatchers.any(String.class))).thenReturn(updatedAuthor1);

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
        when(authorService.update(ArgumentMatchers.any(AuthorDTO.class), ArgumentMatchers.any(String.class))).thenThrow(new AuthorNotFoundException(notFoundAuthorId));

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .put(baseUrl + "/" + notFoundAuthorId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedAuthor));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AuthorNotFoundException))
                .andExpect(result ->
                        assertEquals("Could not find author 51",
                                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void deleteAuthorById_success() throws Exception {
        doNothing().when(authorService).deleteAuthor(author1.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(baseUrl + "/" + author1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void deleteAuthorById_notFound() throws Exception {
        doThrow(new AuthorNotFoundException("51")).when(authorService).deleteAuthor("51");

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(baseUrl + "/51")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof AuthorNotFoundException))
                .andExpect(result ->
                        assertEquals("Could not find author 51", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    private Author getMockAuthor() throws IOException {
        File file = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("Author.json")).getFile());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, Author.class);
    }
}
