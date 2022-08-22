package com.mybooks.bookratingservice.controller;

import com.mybooks.bookratingservice.dto.UserBookRatingRequestDto;
import com.mybooks.bookratingservice.dto.UserBooksRatingsResponseDto;
import com.mybooks.bookratingservice.exception.UserNotFoundException;
import com.mybooks.bookratingservice.service.BookRatingService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/book/rating")
public class BookRatingController {
    private final BookRatingService service;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful user's books ratings getting",
                    response = UserBooksRatingsResponseDto.class, responseContainer = "One element")})
    public UserBooksRatingsResponseDto getUserBooksRatings(
            @Parameter(description = "The userId that needs to get his/her books ratings", required = true)
            @Valid @PathVariable String id
    ) throws UserNotFoundException {
        return service.getBooksRatings(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful new book rating adding", response = void.class)})
    public void addBookRating(
            @Parameter(description = "BookRatingRequestDto object that needs to be saved to the store", required = true)
            @RequestBody @Valid UserBookRatingRequestDto requestDto) throws UserNotFoundException {
        service.addBookRating(requestDto);
    }
}
