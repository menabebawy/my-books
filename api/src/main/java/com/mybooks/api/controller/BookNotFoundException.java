package com.mybooks.api.controller;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String id) {
        super("Could not find book " + id);
    }
}
