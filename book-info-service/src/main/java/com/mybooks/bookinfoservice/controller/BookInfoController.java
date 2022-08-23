package com.mybooks.bookinfoservice.controller;

import com.mybooks.bookinfoservice.dto.BookInfoResponseDto;
import com.mybooks.bookinfoservice.exception.BookNotFoundException;
import com.mybooks.bookinfoservice.service.BookInfoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/book")
public class BookInfoController {
    private final BookInfoService service;

    @GetMapping("/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    public BookInfoResponseDto getBookInfo(@Valid @PathVariable String bookId) throws BookNotFoundException {
        return service.getBookInfo(bookId);
    }
}
