package com.mybooks.api.controller;

import com.mybooks.api.exception.AuthorNotFoundException;
import com.mybooks.api.model.Author;
import com.mybooks.api.reposiotry.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/book/author")
public class AuthorController {
    private final AuthorRepository authorRepository;

    @GetMapping()
    List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @GetMapping("/{id}")
    Author getAuthorById(@PathVariable String id) throws AuthorNotFoundException {
        return authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
    }

    @PostMapping()
    Author createNewAuthor(@RequestBody @Validated Author author) {
        return authorRepository.save(author);
    }

    @PutMapping("/{id}")
    Author updateAuthor(@RequestBody Author updatedAuthor, @PathVariable String id) throws AuthorNotFoundException {
        return authorRepository.findById(id)
                .map( author -> {
                    author.setFirstName(updatedAuthor.getFirstName());
                    author.setLastName(updatedAuthor.getLastName());
                    return authorRepository.save(author);
                })
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    @DeleteMapping("/{id}")
    void deleteAuthor(@PathVariable String id) throws AuthorNotFoundException {
        authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        authorRepository.deleteById(id);
    }
}
