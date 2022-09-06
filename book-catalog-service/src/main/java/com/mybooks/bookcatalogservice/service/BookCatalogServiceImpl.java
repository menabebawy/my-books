package com.mybooks.bookcatalogservice.service;

import com.mybooks.bookcatalogservice.dto.BookCatalogResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@AllArgsConstructor
public class BookCatalogServiceImpl implements BookCatalogService {
    private final RestTemplate restTemplate;

    @Override
    public List<BookCatalogResponseDto> getBooksCatalogs(String userId) {
        return null;
    }
}
