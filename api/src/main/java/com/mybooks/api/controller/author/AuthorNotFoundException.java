package com.mybooks.api.controller.author;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(String id) {
        super("Could not find author " + id);
    }
}
