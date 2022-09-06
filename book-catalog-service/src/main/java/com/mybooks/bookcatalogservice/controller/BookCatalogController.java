package com.mybooks.bookcatalogservice.controller;

import com.mybooks.bookcatalogservice.dto.BookCatalogResponseDto;
import com.mybooks.bookcatalogservice.exception.UserNotFoundException;
import com.mybooks.bookcatalogservice.service.BookCatalogService;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user/book/catalog")
@AllArgsConstructor
public class BookCatalogController {
    final private BookCatalogService service;

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Successful all user's book catalogs retrieval",
                    response = BookCatalogResponseDto.class,
                    responseContainer = "List of user's books")})
    public BookCatalogResponseDto bookCatalog(
            @ApiParam(value = "userId", required = true)
            @PathVariable @Valid String userId
    ) throws UserNotFoundException {
        return null;
    }
}
