package com.mybooks.api.service;

import com.mybooks.api.exception.BookNotFoundException;
import com.mybooks.api.model.Book;
import com.mybooks.api.reposiotry.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class BookServiceImp implements BookService {
    private final BookRepository bookRepository;

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(String id) throws BookNotFoundException {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    @Override
    public Book createNewBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Book updatedBook, String id) throws BookNotFoundException {
        if (getBookById(id) != null) {
            Book book = getBookById(id);
            book.setTitle(updatedBook.getTitle());
            book.setAuthorId(updatedBook.getAuthorId());
            return book;
        } else {
            throw new BookNotFoundException(id);
        }
    }

    @Override
    public void deleteBook(String id) throws BookNotFoundException {
        bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        bookRepository.deleteById(id);
    }
}
