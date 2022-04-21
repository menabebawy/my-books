package com.mybooks.api.service;

import com.mybooks.api.exception.AuthorNotFoundException;
import com.mybooks.api.model.Author;
import com.mybooks.api.reposiotry.AuthorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class AuthorServiceImp implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public Author getAuthorById(String id) throws AuthorNotFoundException {
        return authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
    }

    @Override
    public Author createNewAuthor(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public Author updateAuthor(Author updatedAuthor, String id) throws AuthorNotFoundException {
        return authorRepository.findById(id)
                .map( author -> {
                    author.setFirstName(updatedAuthor.getFirstName());
                    author.setLastName(updatedAuthor.getLastName());
                    return authorRepository.save(author);
                })
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    @Override
    public void deleteAuthor(String id) throws AuthorNotFoundException {
        authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        authorRepository.deleteById(id);
    }
}
