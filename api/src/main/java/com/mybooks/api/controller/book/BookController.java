package com.mybooks.api.controller.book;

import com.mybooks.api.model.Book;
import com.mybooks.api.reposiotry.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookRepository bookRepository;

    @GetMapping("")
    List<Book> allBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    Book getBook(@PathVariable String id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    @PostMapping("")
    Book newBook(@RequestBody @Validated Book book) {
        return bookRepository.save(book);
    }

    @PutMapping("/{id}")
    Book updateBook(@RequestBody Book updatedBook, @PathVariable String id) {
        return bookRepository.findById(id)
                .map( book -> {
                    book.setTitle(updatedBook.getTitle());
                    book.setAuthorId(updatedBook.getAuthorId());
                    return bookRepository.save(book);
                })
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @DeleteMapping("/{id}")
    void deleteBook(@PathVariable String id) {
        bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        bookRepository.deleteById(id);
    }
}
