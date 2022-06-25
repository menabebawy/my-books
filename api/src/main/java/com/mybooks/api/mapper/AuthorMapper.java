package com.mybooks.api.mapper;

import com.mybooks.api.dto.AuthorDTO;
import com.mybooks.api.model.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    Author transformToAuthor(AuthorDTO authorDTO);

    AuthorDTO transformToAuthorDTO(Author author);
}
