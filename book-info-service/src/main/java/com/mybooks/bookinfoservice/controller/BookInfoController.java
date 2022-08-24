package com.mybooks.bookinfoservice.controller;

import com.mybooks.bookinfoservice.dto.BookInfoResponseDto;
import com.mybooks.bookinfoservice.exception.BookNotFoundException;
import com.mybooks.bookinfoservice.service.BookInfoService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
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
    @HystrixCommand(
            fallbackMethod = "fallback_getBookInfo",
            threadPoolKey = "bookInfoPool",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "20"),
                    @HystrixProperty(name = "maxQueueSize", value = "10")
            },
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
            })
    public BookInfoResponseDto getBookInfo(@Valid @PathVariable String bookId) throws BookNotFoundException {
        return service.getBookInfo(bookId);
    }

    public BookInfoResponseDto fallback_getBookInfo(@Valid @PathVariable String bookId) {
        return null;
    }
}
