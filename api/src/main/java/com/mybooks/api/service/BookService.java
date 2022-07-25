package com.mybooks.api.service;

import com.mybooks.api.dto.BookDTO;
import com.mybooks.api.dto.BookRequestDTO;
import com.mybooks.api.exception.BookNotFoundException;

import java.util.List;

public interface BookService {
    List<BookDTO> getAllBooks();

    BookDTO getBookById(String id) throws BookNotFoundException;

    BookDTO addNewBook(BookRequestDTO bookRequestDTO);

    BookDTO updateBook(BookDTO updatedBookDTO, String id) throws BookNotFoundException;

    void deleteBook(String id) throws BookNotFoundException;
}
