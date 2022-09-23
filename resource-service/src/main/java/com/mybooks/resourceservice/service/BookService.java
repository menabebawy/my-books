package com.mybooks.resourceservice.service;

import com.mybooks.resourceservice.dto.BookDTO;
import com.mybooks.resourceservice.dto.BookRequestDTO;
import com.mybooks.resourceservice.exception.BookNotFoundException;

import java.util.List;

public interface BookService {
    List<BookDTO> getAllBooks();

    BookDTO getBookById(String id) throws BookNotFoundException;

    BookDTO addNewBook(BookRequestDTO bookRequestDTO);

    BookDTO updateBook(BookDTO updatedBookDTO, String id) throws BookNotFoundException;

    void deleteBook(String id) throws BookNotFoundException;
}
