package com.mybooks.api.model;

public class MockBook {
    public static Book newBook = Book.builder()
            .id("id")
            .title("Riche dad poor dad")
            .authorId("author_id")
            .build();
}
