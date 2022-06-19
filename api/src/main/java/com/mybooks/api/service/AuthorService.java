package com.mybooks.api.service;

import com.mybooks.api.dto.AuthorDTO;
import com.mybooks.api.exception.AuthorNotFoundException;

import java.util.List;

public interface AuthorService {
    List<AuthorDTO> fetchAll();

    AuthorDTO fetchById(String id) throws AuthorNotFoundException;

    AuthorDTO save(AuthorDTO authorDTO);

    AuthorDTO update(AuthorDTO updatedAuthorDTO, String id) throws AuthorNotFoundException;

    void deleteAuthor(String id) throws AuthorNotFoundException;
}
