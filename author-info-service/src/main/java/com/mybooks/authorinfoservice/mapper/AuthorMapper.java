package com.mybooks.authorinfoservice.mapper;

import com.mybooks.authorinfoservice.dto.AuthorDTO;
import com.mybooks.authorinfoservice.model.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    Author transformToAuthor(AuthorDTO authorDTO);

    AuthorDTO transformToAuthorDTO(Author author);
}
