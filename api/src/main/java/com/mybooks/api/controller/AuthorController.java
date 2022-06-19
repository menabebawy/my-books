package com.mybooks.api.controller;

import com.mybooks.api.dto.AuthorDTO;
import com.mybooks.api.exception.AuthorNotFoundException;
import com.mybooks.api.model.Author;
import com.mybooks.api.service.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        return new ResponseEntity<>(authorService.fetchAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<AuthorDTO> getAuthorById(@PathVariable String id) throws AuthorNotFoundException {
        return new ResponseEntity<>(authorService.fetchById(id), HttpStatus.OK);
    }

    @PostMapping()
    ResponseEntity<AuthorDTO> addAuthor(@Valid @RequestBody AuthorDTO authorDTO) {
        return new ResponseEntity<>(authorService.save(authorDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    ResponseEntity<AuthorDTO> updateAuthor(@RequestBody @Valid AuthorDTO updatedAuthorDTO, @PathVariable String id) throws AuthorNotFoundException {
        return new ResponseEntity<>(authorService.update(updatedAuthorDTO, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity deleteAuthor(@PathVariable String id) throws AuthorNotFoundException {
        authorService.deleteAuthor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
