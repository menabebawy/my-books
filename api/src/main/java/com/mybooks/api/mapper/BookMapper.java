package com.mybooks.api.mapper;

import com.mybooks.api.dto.BookDTO;
import com.mybooks.api.model.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDTO transformToBookDTO(Book book);

    Book transformToBook(BookDTO bookDTO);
}
