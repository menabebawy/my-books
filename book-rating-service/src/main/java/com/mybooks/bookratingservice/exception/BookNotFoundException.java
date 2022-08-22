package com.mybooks.bookratingservice.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String id) {
        super("Could not find book " + id);
    }
}
