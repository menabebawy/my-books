package com.mybooks.api.controller;

import com.mybooks.api.model.Author;
import com.mybooks.api.reposiotry.AuthorRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorRepository authorRepository;

    @GetMapping("")
    List<Author> getAll() {
        return authorRepository.findAll();
    }

    @GetMapping("/{id}")
    Author getAuthor(@RequestParam String id) {
        return authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
    }

    @PostMapping("")
    Author newAuthor(@RequestBody Author author) {
        return authorRepository.save(author);
    }
}
