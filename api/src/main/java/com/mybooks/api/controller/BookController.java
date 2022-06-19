package com.mybooks.api.controller;

import com.mybooks.api.dto.BookDTO;
import com.mybooks.api.exception.BookNotFoundException;
import com.mybooks.api.model.Book;
import com.mybooks.api.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    ResponseEntity<List<BookDTO>> getAllBooks() {
        return new ResponseEntity<>(bookService.fetchAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<BookDTO> getBookById(@PathVariable String id) throws BookNotFoundException {
        return new ResponseEntity<>(bookService.fetchById(id), HttpStatus.OK);
    }

    @PostMapping()
    ResponseEntity<BookDTO> createNewBook(@RequestBody @Validated BookDTO book) {
        return new ResponseEntity<>(bookService.save(book), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    ResponseEntity<BookDTO> updateBook(@RequestBody BookDTO updatedBookDTO, @PathVariable String id) throws BookNotFoundException {
        return new ResponseEntity<>(bookService.update(updatedBookDTO, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity deleteBook(@PathVariable String id) throws BookNotFoundException {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
