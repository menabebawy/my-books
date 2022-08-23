package com.mybooks.authorinfoservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybooks.authorinfoservice.dto.AuthorDTO;
import com.mybooks.authorinfoservice.dto.AuthorRequestDTO;
import com.mybooks.authorinfoservice.exception.AuthorNotFoundException;
import com.mybooks.authorinfoservice.mapper.AuthorMapper;
import com.mybooks.authorinfoservice.model.Author;
import com.mybooks.authorinfoservice.service.AuthorServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class AuthorControllerTest {
    private final String baseUrl = "/book/author";
    private final String notFoundAuthorId = "51";
    private final AuthorDTO author1 = AuthorDTO.builder().id("Author1_id").firstName("Sam").lastName("Simon").build();
    private final AuthorDTO author2 = AuthorDTO.builder().id("Author2_id").firstName("Adi").lastName("John").build();
    private final AuthorDTO author3 = AuthorDTO.builder().id("Author3_id").firstName("Dodo").lastName("Adel").build();
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    AuthorServiceImpl authorService;
    @Autowired
    AuthorMapper authorMapper;

    @Test
    void getAllAuthors_success() throws Exception {
        List<AuthorDTO> authors = new ArrayList<>(Arrays.asList(author1, author2, author3));

        when(authorService.getAllAuthors()).thenReturn(authors);

        mockMvc.perform(get(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[1].firstName", Matchers.is("Adi")))
                .andExpect(jsonPath("$[1].lastName", Matchers.is("John")));
    }

    @Test
    void getAuthorById_success() throws Exception {
        when(authorService.getAuthorById(author1.getId())).thenReturn(author1);

        mockMvc.perform(get(baseUrl + "/" + author1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.firstName", Matchers.is(author1.getFirstName())))
                .andExpect(jsonPath("$.lastName", Matchers.is(author1.getLastName())));
    }

    @Test
    void whenGetAuthorRequestByNotFoundId_thenCorrectResponse() throws Exception {
        when(authorService.getAuthorById(notFoundAuthorId)).thenThrow(new AuthorNotFoundException(notFoundAuthorId));

        mockMvc.perform(get(baseUrl + "/" + notFoundAuthorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("message", Matchers.is("Could not find author " + notFoundAuthorId)));
    }

    @Test
    void addNewAuthor_success() throws Exception {
        AuthorRequestDTO authorRequestDTO = AuthorRequestDTO.builder()
                .firstName(getMockAuthor().getFirstName())
                .lastName(getMockAuthor().getLastName())
                .build();

        AuthorDTO authorDTO = AuthorDTO.builder()
                .firstName(authorRequestDTO.getFirstName())
                .lastName(authorRequestDTO.getLastName())
                .build();

        when(authorService.addAuthor(ArgumentMatchers.any(AuthorRequestDTO.class))).thenReturn(authorDTO);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(authorDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.firstName", Matchers.is(authorRequestDTO.getFirstName())))
                .andExpect(jsonPath("$.lastName", Matchers.is(authorRequestDTO.getLastName())));
    }

    @Test
    void whenPostRequestToAuthorAndInValidAuthorFirstName_thenCorrectResponse() throws Exception {
        Author author = getMockAuthor();
        author.setFirstName(null);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(author)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("message", Matchers.is("firstName is required")));
    }

    @Test
    void whenPostRequestToAuthorAndInValidAuthorLastName_thenCorrectResponse() throws Exception {
        Author author = getMockAuthor();
        author.setLastName(null);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(author)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("message", Matchers.is("lastName is required")));
    }

    @Test
    void updateAuthor_success() throws Exception {
        AuthorDTO updatedAuthor1 = AuthorDTO.builder()
                .id(author1.getId())
                .firstName("Dark")
                .lastName("Tim")
                .build();
        when(authorService.updateAuthor(ArgumentMatchers.any(AuthorDTO.class), ArgumentMatchers.any(String.class))).thenReturn(updatedAuthor1);

        mockMvc.perform(put(baseUrl + "/" + author1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(updatedAuthor1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.firstName", Matchers.is(updatedAuthor1.getFirstName())))
                .andExpect(jsonPath("$.lastName", Matchers.is(updatedAuthor1.getLastName())));
    }

    @Test
    void updateAuthor_notFound() throws Exception {
        Author updatedAuthor = getMockAuthor();
        when(authorService.updateAuthor(ArgumentMatchers.any(AuthorDTO.class), ArgumentMatchers.any(String.class))).thenThrow(new AuthorNotFoundException(notFoundAuthorId));

        mockMvc.perform(put(baseUrl + "/" + notFoundAuthorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(updatedAuthor)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AuthorNotFoundException))
                .andExpect(result ->
                        assertEquals("Could not find author 51",
                                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void whenPutRequestToAuthorAndInvalidAuthorFirstName_thenCorrectResponse() throws Exception {
        AuthorDTO updatedAuthor = AuthorDTO.builder()
                .id(author1.getId())
                .firstName(null)
                .lastName("Tim")
                .build();

        mockMvc.perform(put(baseUrl + "/" + updatedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(updatedAuthor)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("message", Matchers.is("firstName is required")));
    }

    @Test
    void whenPutRequestToAuthorAndInvalidAuthorLastName_thenCorrectResponse() throws Exception {
        AuthorDTO updatedAuthor = AuthorDTO.builder()
                .id(author1.getId())
                .firstName("Tim")
                .lastName(null)
                .build();

        mockMvc.perform(put(baseUrl + "/" + updatedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(updatedAuthor)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("message", Matchers.is("lastName is required")));
    }

    @Test
    void deleteAuthorById_success() throws Exception {
        doNothing().when(authorService).deleteAuthor(author1.getId());

        mockMvc.perform(delete(baseUrl + "/" + author1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAuthorById_notFound() throws Exception {
        doThrow(new AuthorNotFoundException("51")).when(authorService).deleteAuthor("51");

        mockMvc.perform(delete(baseUrl + "/51")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
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
