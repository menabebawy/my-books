package com.mybooks.api.service;

import com.mybooks.api.exception.BookNotFoundException;
import com.mybooks.api.model.Book;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();

    Book getBookById(String id) throws BookNotFoundException;

    Book createNewBook(Book book);

    Book updateBook(Book updatedBook, String id) throws BookNotFoundException;

    void deleteBook(String id) throws BookNotFoundException;
}
