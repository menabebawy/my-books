package com.mybooks.bookcatalogservice.service;

import com.mybooks.bookcatalogservice.dto.BookCatalogResponseDto;

import java.util.List;

public interface BookCatalogService {
    List<BookCatalogResponseDto> getBooksCatalogs(String userId);
}
