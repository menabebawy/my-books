package com.mybooks.api.controller;

import com.mybooks.api.exception.BookNotFoundException;
import com.mybooks.api.model.Book;
import com.mybooks.api.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping()
    List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    Book getBookById(@PathVariable String id) throws BookNotFoundException {
        return bookService.getBookById(id);
    }

    @PostMapping()
    Book createNewBook(@RequestBody @Validated Book book) {
        return bookService.createNewBook(book);
    }

    @PutMapping("/{id}")
    Book updateBook(@RequestBody Book updatedBook, @PathVariable String id) throws BookNotFoundException {
        return bookService.updateBook(updatedBook, id);
    }

    @DeleteMapping("/{id}")
    void deleteBook(@PathVariable String id) throws BookNotFoundException {
        bookService.deleteBook(id);
    }
}
