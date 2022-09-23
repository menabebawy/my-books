package com.mybooks.resourceservice.mapper;

import com.mybooks.resourceservice.dto.AuthorDTO;
import com.mybooks.resourceservice.model.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    Author transformToAuthor(AuthorDTO authorDTO);

    AuthorDTO transformToAuthorDTO(Author author);
}
