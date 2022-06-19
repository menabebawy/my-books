package com.mybooks.api.service;

import com.mybooks.api.dto.BookDTO;
import com.mybooks.api.exception.BookNotFoundException;

import java.util.List;

public interface BookService {
    List<BookDTO> fetchAll();

    BookDTO fetchById(String id) throws BookNotFoundException;

    BookDTO save(BookDTO bookDTO);

    BookDTO update(BookDTO updatedBookDTO, String id) throws BookNotFoundException;

    void deleteBook(String id) throws BookNotFoundException;
}
