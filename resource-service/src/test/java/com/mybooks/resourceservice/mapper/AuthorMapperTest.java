package com.mybooks.resourceservice.mapper;

import com.mybooks.api.dto.AuthorDTO;
import com.mybooks.api.model.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuthorMapperTest {
    private AuthorMapper authorMapper;

    @BeforeEach
    void setup() {
        authorMapper = new AuthorMapperImpl();
    }

    @Test
    void givenAuthorEntityToAuthorDTO_whenMap_thenCorrect() {
        Author author = new Author();
        author.setId("author_id");
        author.setFirstName("author_firstName");
        author.setLastName("author_LastName");
        AuthorDTO authorDTO = authorMapper.transformToAuthorDTO(author);
        assertEquals(author.getId(), authorDTO.getId());
        assertEquals(author.getFirstName(), authorDTO.getFirstName());
        assertEquals(author.getLastName(), authorDTO.getLastName());
    }

    @Test
    void givenAuthorDTOToAuthorEntity_whenMap_thenCorrect() {
        AuthorDTO authorDTO = AuthorDTO.builder()
                .id("author_id")
                .firstName("author_firstName")
                .lastName("author_LastName")
                .build();
        Author author = authorMapper.transformToAuthor(authorDTO);
        assertEquals(author.getId(), authorDTO.getId());
        assertEquals(author.getFirstName(), authorDTO.getFirstName());
        assertEquals(author.getLastName(), authorDTO.getLastName());
    }
}
