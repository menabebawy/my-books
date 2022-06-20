package com.mybooks.api.controller;

import com.mybooks.api.dto.BookDTO;
import com.mybooks.api.exception.BookNotFoundException;
import com.mybooks.api.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    ResponseEntity<BookDTO> getBookById(@Valid @PathVariable String id) throws BookNotFoundException {
        return new ResponseEntity<>(bookService.fetchById(id), HttpStatus.OK);
    }

    @PostMapping()
    ResponseEntity<BookDTO> createNewBook(@Valid @RequestBody BookDTO book) {
        return new ResponseEntity<>(bookService.save(book), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    ResponseEntity<BookDTO> updateBook(@Valid @RequestBody BookDTO updatedBookDTO, @Valid @PathVariable String id) throws BookNotFoundException {
        return new ResponseEntity<>(bookService.update(updatedBookDTO, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity deleteBook(@PathVariable String id) throws BookNotFoundException {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
