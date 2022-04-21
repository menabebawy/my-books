package com.mybooks.api.service;

import com.mybooks.api.exception.AuthorNotFoundException;
import com.mybooks.api.model.Author;

import java.util.List;

public interface AuthorService {
    List<Author> getAllAuthors();

    Author getAuthorById(String id) throws AuthorNotFoundException;

    Author createNewAuthor(Author author);

    Author updateAuthor(Author updatedAuthor, String id) throws AuthorNotFoundException;

    void deleteAuthor(String id) throws AuthorNotFoundException;
}
