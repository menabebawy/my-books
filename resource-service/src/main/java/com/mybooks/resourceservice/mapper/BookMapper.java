package com.mybooks.resourceservice.mapper;

import com.mybooks.resourceservice.dto.BookDTO;
import com.mybooks.resourceservice.model.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDTO transformToBookDTO(Book book);

    Book transformToBook(BookDTO bookDTO);
}
