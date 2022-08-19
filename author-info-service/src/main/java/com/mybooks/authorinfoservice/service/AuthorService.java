package com.mybooks.authorinfoservice.service;

import com.mybooks.authorinfoservice.dto.AuthorDTO;
import com.mybooks.authorinfoservice.dto.AuthorRequestDTO;
import com.mybooks.authorinfoservice.exception.AuthorNotFoundException;

import java.util.List;

public interface AuthorService {
    List<AuthorDTO> getAllAuthors();

    AuthorDTO getAuthorById(String id) throws AuthorNotFoundException;

    AuthorDTO addAuthor(AuthorRequestDTO requestDTO);

    AuthorDTO updateAuthor(AuthorDTO updatedAuthorDTO, String id) throws AuthorNotFoundException;

    void deleteAuthor(String id) throws AuthorNotFoundException;
}
