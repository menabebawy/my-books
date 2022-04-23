package com.mybooks.api.model;

import com.mybooks.api.model.Author;

public class MockAuthor {
    public static Author newAuthor = Author.builder()
            .id("id")
            .firstName("Time")
            .lastName("Thomas")
            .build();
}
