package com.mybooks.resourceservice.controller;

import com.mybooks.resourceservice.dto.AuthorDTO;
import com.mybooks.resourceservice.dto.AuthorRequestDTO;
import com.mybooks.resourceservice.exception.AuthorNotFoundException;
import com.mybooks.resourceservice.service.AuthorService;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/book/author")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful all authors retrieval",
                    response = AuthorDTO.class, responseContainer = "List")})
    List<AuthorDTO> getAllAuthors() {
        return authorService.getAllAuthors();
    }


    @PreAuthorize("hasAuthority('DEVELOPER')")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful author retrieval by Id",
                    response = AuthorDTO.class, responseContainer = "One element")})
    AuthorDTO getAuthorById(@ApiParam(value = "authorId", required = true)
                            @PathVariable String id) throws AuthorNotFoundException {
        return authorService.getAuthorById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful new author adding",
                    response = AuthorDTO.class, responseContainer = "One element")})
    AuthorDTO addAuthor(@Valid @RequestBody AuthorRequestDTO requestDTO) {
        return authorService.addAuthor(requestDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful author updating by Id",
                    response = AuthorDTO.class, responseContainer = "One element")})
    AuthorDTO updateAuthor(@Valid @RequestBody AuthorDTO updatedAuthorDTO, @Valid @PathVariable String id) throws AuthorNotFoundException {
        return authorService.updateAuthor(updatedAuthorDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {@ApiResponse(code = 204, message = "Successful author deleting by Id")})
    void deleteAuthor(@ApiParam(value = "authorId", required = true)
                      @PathVariable String id) throws AuthorNotFoundException {
        authorService.deleteAuthor(id);
    }
}
