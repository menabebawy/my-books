package com.mybooks.bookcatalogservice.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Book {
    String id;
    String title;
    Author author;
}
