package com.mybooks.bookinfoservice.mapper;

import com.mybooks.bookinfoservice.dto.BookDto;
import com.mybooks.bookinfoservice.model.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookInfoMapper {
    BookDto transferToBookDto(Book book);
}
