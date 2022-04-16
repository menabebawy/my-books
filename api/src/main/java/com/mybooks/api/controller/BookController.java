package com.mybooks.api.controller;

import com.mybooks.api.model.Book;
import com.mybooks.api.reposiotry.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookRepository bookRepository;

    @GetMapping("")
    List<Book> allBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    Book getBook(@PathVariable String id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    @PostMapping("")
    Book newBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }
}
