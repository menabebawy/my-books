package com.mybooks.resourceserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class Product {
    int id;
    String name;
    int quantity;
}
