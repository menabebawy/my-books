package com.mybooks.bookcatalogservice.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Author {
    String id;
    String firstName;
    String lastName;
}
