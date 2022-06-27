package com.mybooks.api.controller;

import com.mybooks.api.dto.BookDTO;
import com.mybooks.api.exception.BookNotFoundException;
import com.mybooks.api.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    @ResponseStatus(HttpStatus.OK)
    List<BookDTO> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    BookDTO getBookById(@Valid @PathVariable String id) throws BookNotFoundException {
        return bookService.getBookById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    BookDTO createNewBook(@Valid @RequestBody BookDTO book) {
        return bookService.addNewBook(book);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    BookDTO updateBook(@Valid @RequestBody BookDTO updatedBookDTO, @Valid @PathVariable String id) throws BookNotFoundException {
        return bookService.updateBook(updatedBookDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteBook(@PathVariable String id) throws BookNotFoundException {
        bookService.deleteBook(id);
    }
}
