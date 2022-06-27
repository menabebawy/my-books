package com.mybooks.api.service;

import com.mybooks.api.dto.AuthorDTO;
import com.mybooks.api.exception.AuthorNotFoundException;

import java.util.List;

public interface AuthorService {
    List<AuthorDTO> getAllAuthors();

    AuthorDTO getAuthorById(String id) throws AuthorNotFoundException;

    AuthorDTO addAuthor(AuthorDTO authorDTO);

    AuthorDTO updateAuthor(AuthorDTO updatedAuthorDTO, String id) throws AuthorNotFoundException;

    void deleteAuthor(String id) throws AuthorNotFoundException;
}
