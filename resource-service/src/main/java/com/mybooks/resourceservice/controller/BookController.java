package com.mybooks.resourceservice.controller;

import com.mybooks.resourceservice.dto.BookDTO;
import com.mybooks.resourceservice.dto.BookRequestDTO;
import com.mybooks.resourceservice.exception.BookNotFoundException;
import com.mybooks.resourceservice.service.BookService;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
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
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful book retrieval by Id",
                    response = BookDTO.class, responseContainer = "One element")})
    BookDTO getBookById(@ApiParam(value = "bookId", required = true)
                        @Valid @PathVariable String id) throws BookNotFoundException {
        return bookService.getBookById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful new book adding",
                    response = BookDTO.class, responseContainer = "One element")})
    BookDTO createNewBook(
            @Parameter(description = "BookRequestDTO object that needs to be saved to the store", required = true)
            @Valid @RequestBody BookRequestDTO bookRequestDTO) {
        return bookService.addNewBook(bookRequestDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful book updating by Id",
                    response = BookDTO.class, responseContainer = "One element")})
    BookDTO updateBook(
            @Parameter(description = "BookDTO object that needs to be updated to the store", required = true)
            @Valid @RequestBody BookDTO updatedBookDTO,
            @Parameter(description = "The bookId of the book that needs to be updated", required = true)
            @Valid @PathVariable String id) throws BookNotFoundException {
        return bookService.updateBook(updatedBookDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {@ApiResponse(code = 204, message = "Successful deleted book by Id")})
    void deleteBook(@Parameter(description = "The bookId of the book that needs to be deleted", required = true)
                    @PathVariable String id) throws BookNotFoundException {
        bookService.deleteBook(id);
    }
}
