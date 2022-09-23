package com.mybooks.resourceservice.service;

import com.mybooks.resourceservice.dto.AuthorDTO;
import com.mybooks.resourceservice.dto.AuthorRequestDTO;
import com.mybooks.resourceservice.exception.AuthorNotFoundException;

import java.util.List;

public interface AuthorService {
    List<AuthorDTO> getAllAuthors();

    AuthorDTO getAuthorById(String id) throws AuthorNotFoundException;

    AuthorDTO addAuthor(AuthorRequestDTO requestDTO);

    AuthorDTO updateAuthor(AuthorDTO updatedAuthorDTO, String id) throws AuthorNotFoundException;

    void deleteAuthor(String id) throws AuthorNotFoundException;
}
