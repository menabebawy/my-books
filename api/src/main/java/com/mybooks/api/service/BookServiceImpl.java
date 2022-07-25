package com.mybooks.api.service;

import com.mybooks.api.dto.BookDTO;
import com.mybooks.api.dto.BookRequestDTO;
import com.mybooks.api.exception.BookNotFoundException;
import com.mybooks.api.mapper.BookMapper;
import com.mybooks.api.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public List<BookDTO> getAllBooks() {
        return StreamSupport.stream(bookRepository.findAll().spliterator(), false)
                .map(bookMapper::transformToBookDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookDTO getBookById(String id) throws BookNotFoundException {
        return bookRepository.findById(id)
                .map(bookMapper::transformToBookDTO)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Override
    public BookDTO addNewBook(BookRequestDTO bookRequestDTO) {
        BookDTO bookDTO = BookDTO.builder()
                .title(bookRequestDTO.getTitle())
                .authorId(bookRequestDTO.getAuthorId())
                .build();
        return Optional.of(bookDTO)
                .map(bookMapper::transformToBook)
                .map(bookRepository::save)
                .map(bookMapper::transformToBookDTO)
                .orElseThrow();
    }

    @Override
    public BookDTO updateBook(BookDTO updatedBookDTO, String id) throws BookNotFoundException {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(updatedBookDTO.getTitle());
                    book.setAuthorId(updatedBookDTO.getAuthorId());
                    return book;
                })
                .map(bookRepository::save)
                .map(bookMapper::transformToBookDTO)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Override
    public void deleteBook(String id) throws BookNotFoundException {
        getBookById(id);
        bookRepository.deleteById(id);
    }
}
