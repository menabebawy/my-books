package com.mybooks.api.controller;

import com.mybooks.api.exception.AuthorNotFoundException;
import com.mybooks.api.model.Author;
import com.mybooks.api.service.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/book/author")
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping()
    List<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/{id}")
    Author getAuthorById(@PathVariable String id) throws AuthorNotFoundException {
        return authorService.getAuthorById(id);
    }

    @PostMapping()
    Author createNewAuthor(@Valid @RequestBody Author author) {
        return authorService.addAuthor(author);
    }

    @PutMapping("/{id}")
    Author updateAuthor(@RequestBody @Valid Author updatedAuthor, @PathVariable String id) throws AuthorNotFoundException {
        return authorService.updateAuthor(updatedAuthor, id);
    }

    @DeleteMapping("/{id}")
    void deleteAuthor(@PathVariable String id) throws AuthorNotFoundException {
        authorService.deleteAuthor(id);
    }
}
