package com.mybooks.resourceservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybooks.resourceservice.dto.AuthorDTO;
import com.mybooks.resourceservice.dto.AuthorRequestDTO;
import com.mybooks.resourceservice.exception.AuthorNotFoundException;
import com.mybooks.resourceservice.mapper.AuthorMapper;
import com.mybooks.resourceservice.mapper.AuthorMapperImpl;
import com.mybooks.resourceservice.model.Author;
import com.mybooks.resourceservice.repository.AuthorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
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
class AuthorServiceTest {
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
        assertEquals(authorService.getAllAuthors().size(), authorDTOList.size());
    }

    @Test
    void getAuthorById_success() throws IOException {
        Author author = getMockAuthor();
        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
        assertDoesNotThrow(() -> authorService.getAuthorById(author.getId()));
        AuthorDTO createdAuthor = authorService.getAuthorById(author.getId());
        assertEquals(createdAuthor.getId(), author.getId());
        assertEquals(createdAuthor.getFirstName(), author.getFirstName());
    }

    @Test
    void getAuthorById_notFound() {
        when(authorRepository.findById(notFoundAuthorId)).thenThrow(new AuthorNotFoundException(notFoundAuthorId));
        assertThrows(AuthorNotFoundException.class, () -> authorService.getAuthorById(notFoundAuthorId));
        verify(authorRepository, Mockito.times(1)).findById(notFoundAuthorId);
    }

    @Test
    void addNewAuthor_success() throws IOException {
        AuthorRequestDTO authorRequestDTO = AuthorRequestDTO.builder()
                .firstName(getMockAuthor().getFirstName())
                .lastName(getMockAuthor().getLastName())
                .build();
        Author author = getMockAuthor();
        when(authorRepository.save(ArgumentMatchers.any(Author.class))).thenReturn(author);
        AuthorDTO createdAuthor = authorService.addAuthor(authorRequestDTO);
        assertEquals(createdAuthor.getFirstName(), author.getFirstName());
        assertEquals(createdAuthor.getId(), author.getId());
        verify(authorRepository, Mockito.times(1)).save(ArgumentMatchers.any(Author.class));
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
        AuthorDTO returnedAuthorDTOAfterUpdated = authorService.updateAuthor(updatedAuthorDTO, author.getId());
        assertDoesNotThrow(() -> authorService.updateAuthor(updatedAuthorDTO, author.getId()));
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
        assertThrows(AuthorNotFoundException.class, () -> authorService.updateAuthor(updatedAuthor, updatedAuthor.getId()));
    }

    @Test
    void deleteAuthor_notFound() {
        doThrow(AuthorNotFoundException.class).when(authorRepository).deleteById(notFoundAuthorId);
        assertThrows(AuthorNotFoundException.class, () -> authorService.deleteAuthor(notFoundAuthorId));
    }

    @Test
    void deleteAuthor_success() throws IOException {
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