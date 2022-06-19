package com.mybooks.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybooks.api.dto.AuthorDTO;
import com.mybooks.api.exception.AuthorNotFoundException;
import com.mybooks.api.mapper.AuthorMapper;
import com.mybooks.api.mapper.AuthorMapperImpl;
import com.mybooks.api.model.Author;
import com.mybooks.api.repository.AuthorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthorServiceTest {
    private final String notFoundAuthorId = "51";
    @Mock
    AuthorRepository authorRepository;

    private AuthorMapper authorMapper;
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        authorMapper = new AuthorMapperImpl();
        authorService = new AuthorServiceImpl(authorRepository, authorMapper);
    }

    @AfterEach
    void destroyAll() {
        authorRepository.deleteAll();
    }

    @Test
    void getAllAuthors_success() {
        AuthorDTO author1 = AuthorDTO.builder().id("Author1_id").firstName("Sam").lastName("Simon").build();
        AuthorDTO author2 = AuthorDTO.builder().id("Author2_id").firstName("Adi").lastName("John").build();
        AuthorDTO author3 = AuthorDTO.builder().id("Author3_id").firstName("Dodo").lastName("Adel").build();
        List<AuthorDTO> authorDTOList = new ArrayList<>(Arrays.asList(author1, author2, author3));
        List<Author> authorsList = authorDTOList.stream().map(authorMapper::transformToAuthor).collect(Collectors.toList());
        when(authorRepository.findAll()).thenReturn(authorsList);
        assertEquals(authorService.fetchAll().size(), authorDTOList.size());
    }

    @Test
    void getAuthorById_success() throws IOException {
        Author author = getMockAuthor();
        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
        assertDoesNotThrow(() -> authorService.fetchById(author.getId()));
        AuthorDTO createdAuthor = authorService.fetchById(author.getId());
        assertEquals(createdAuthor.getId(), author.getId());
        assertEquals(createdAuthor.getFirstName(), author.getFirstName());
    }

    @Test
    void getAuthorById_notFound() {
        when(authorRepository.findById(notFoundAuthorId)).thenThrow(new AuthorNotFoundException(notFoundAuthorId));
        assertThrows(AuthorNotFoundException.class, () -> authorService.fetchById(notFoundAuthorId));
        verify(authorRepository, Mockito.times(1)).findById(notFoundAuthorId);
    }

    @Test
    void addNewAuthor_success() throws IOException {
        Author author = getMockAuthor();
        when(authorRepository.save(author)).thenReturn(author);
        AuthorDTO authorDTO = authorMapper.transformToAuthorDTO(author);
        AuthorDTO createdAuthor = authorService.save(authorDTO);
        assertEquals(createdAuthor.getFirstName(), author.getFirstName());
        assertEquals(createdAuthor.getId(), author.getId());
        verify(authorRepository, Mockito.times(1)).save(author);
    }

    @Test
    void updateAuthor_success() throws IOException {
        Author author = getMockAuthor();
        AuthorDTO updatedAuthorDTO = AuthorDTO.builder()
                .id(author.getId())
                .firstName("New First Name")
                .lastName(author.getLastName())
                .build();
        Author updatedAuthor = authorMapper.transformToAuthor(updatedAuthorDTO);
        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
        when(authorRepository.save(author)).thenReturn(updatedAuthor);
        AuthorDTO returnedAuthorDTOAfterUpdated = authorService.update(updatedAuthorDTO, author.getId());
        assertDoesNotThrow(() -> authorService.update(updatedAuthorDTO, author.getId()));
        assertEquals(returnedAuthorDTOAfterUpdated.getFirstName(), updatedAuthorDTO.getFirstName());
    }

    @Test
    void updateAuthor_notFound() throws IOException {
        AuthorDTO updatedAuthor = AuthorDTO.builder()
                .id(getMockAuthor().getId())
                .firstName("New first name")
                .lastName(getMockAuthor().getLastName())
                .build();
        when(authorRepository.findById(updatedAuthor.getId())).thenThrow(new AuthorNotFoundException(updatedAuthor.getId()));
        assertThrows(AuthorNotFoundException.class, () -> authorService.update(updatedAuthor, updatedAuthor.getId()));
    }

    @Test
    public void deleteAuthor_notFound() {
        doThrow(AuthorNotFoundException.class).when(authorRepository).deleteById(notFoundAuthorId);
        assertThrows(AuthorNotFoundException.class, () -> authorService.deleteAuthor(notFoundAuthorId));
    }

    @Test
    public void deleteAuthor_success() throws IOException {
        Author author = getMockAuthor();
        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
        doNothing().when(authorRepository).deleteById(author.getId());
        assertDoesNotThrow(() -> authorService.deleteAuthor(author.getId()));
    }

    private Author getMockAuthor() throws IOException {
        File file = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("Author.json")).getFile());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, Author.class);
    }
}