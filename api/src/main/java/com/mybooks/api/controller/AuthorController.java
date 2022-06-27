package com.mybooks.api.controller;

import com.mybooks.api.dto.AuthorDTO;
import com.mybooks.api.exception.AuthorNotFoundException;
import com.mybooks.api.service.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/book/author")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    List<AuthorDTO> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    AuthorDTO getAuthorById(@PathVariable String id) throws AuthorNotFoundException {
        return authorService.getAuthorById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    AuthorDTO addAuthor(@Valid @RequestBody AuthorDTO authorDTO) {
        return authorService.addAuthor(authorDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    AuthorDTO updateAuthor(@Valid @RequestBody AuthorDTO updatedAuthorDTO, @Valid @PathVariable String id) throws AuthorNotFoundException {
        return authorService.updateAuthor(updatedAuthorDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteAuthor(@PathVariable String id) throws AuthorNotFoundException {
        authorService.deleteAuthor(id);
    }
}
