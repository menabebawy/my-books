package com.mybooks.api.controller;

import com.mybooks.api.exception.BookNotFoundException;
import com.mybooks.api.model.Book;
import com.mybooks.api.reposiotry.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/book")
public class BookController {
    private final BookRepository bookRepository;

    @GetMapping()
    List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    Book getBookById(@PathVariable String id) throws BookNotFoundException {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    @PostMapping()
    Book createNewBook(@RequestBody @Validated Book book) {
        return bookRepository.save(book);
    }

    @PutMapping("/{id}")
    Book updateBook(@RequestBody Book updatedBook, @PathVariable String id) throws BookNotFoundException {
        return bookRepository.findById(id)
                .map( book -> {
                    book.setTitle(updatedBook.getTitle());
                    book.setAuthorId(updatedBook.getAuthorId());
                    return bookRepository.save(book);
                })
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @DeleteMapping("/{id}")
    void deleteBook(@PathVariable String id) throws BookNotFoundException {
        bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        bookRepository.deleteById(id);
    }
}
