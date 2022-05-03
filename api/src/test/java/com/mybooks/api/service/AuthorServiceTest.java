package com.mybooks.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybooks.api.exception.AuthorNotFoundException;
import com.mybooks.api.model.Author;
import com.mybooks.api.reposiotry.AuthorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthorServiceTest {
    @Mock
    AuthorRepository authorRepository;

    private AuthorService authorService;

    private final String notFoundAuthorId = "51";

    @BeforeEach
    void setUp() {
        authorService = new AuthorServiceImp(authorRepository);
    }

    @AfterEach
    void destroyAll(){
        authorRepository.deleteAll();
    }

    @Test
    void getAllAuthors_success() {
        Author author1 = new Author("Author1_id", "Sam", "Simon");
        Author author2 = new Author("Author2_id", "Adi", "John");
        Author author3 = new Author("Author3_id", "Dodo", "Adel");
        List<Author> authors = new ArrayList<>(Arrays.asList(author1, author2, author3));
        when(authorRepository.findAll()).thenReturn(authors);
        assertEquals(authorService.getAllAuthors().size(), authors.size());
    }

    @Test
    void getAuthorById_success() throws IOException {
        Author author = getMockAuthor();
        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
        assertDoesNotThrow(() -> authorService.getAuthorById(author.getId()));
        Author createdAuthor = authorService.getAuthorById(author.getId());
        assertEquals(createdAuthor.getId(), author.getId());
        assertEquals(createdAuthor.getFirstName(), author.getFirstName());
    }

    @Test
    void getAuthorById_notFound() {
        when(authorRepository.findById(notFoundAuthorId)).thenThrow(new AuthorNotFoundException(notFoundAuthorId));
        assertThrows(AuthorNotFoundException.class, () -> authorService.getAuthorById(notFoundAuthorId));
        verify(authorRepository, times(1)).findById(notFoundAuthorId);
    }

    @Test
    void createNewAuthor_success() throws IOException {
        Author author = getMockAuthor();
        when(authorRepository.save(author)).thenReturn(author);
        Author createdAuthor = authorService.addAuthor(author);
        assertEquals(createdAuthor.getFirstName(), author.getFirstName());
        assertEquals(createdAuthor.getId(), author.getId());
        verify(authorRepository, times(1)).save(author);
    }

    @Test
    void updateAuthor_success() throws IOException {
        Author author = getMockAuthor();
        Author updatedAuthor = getMockAuthor();
        updatedAuthor.setFirstName("New first name");
        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
        assertDoesNotThrow(() -> authorService.updateAuthor(updatedAuthor, author.getId()));
        assertEquals(authorService.updateAuthor(updatedAuthor, author.getId()).getFirstName(), updatedAuthor.getFirstName());
    }

    @Test
    void updateAuthor_notFound() throws IOException {
        Author updatedAuthor = getMockAuthor();
        updatedAuthor.setFirstName("New first name");
        when(authorRepository.findById(updatedAuthor.getId())).thenThrow(new AuthorNotFoundException(updatedAuthor.getId()));
        assertThrows(AuthorNotFoundException.class, () -> authorService.updateAuthor(updatedAuthor, updatedAuthor.getId()));
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