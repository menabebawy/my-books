package com.mybooks.bookinfoservice.service;

import com.mybooks.bookinfoservice.dto.AuthorDto;
import com.mybooks.bookinfoservice.dto.BookDto;
import com.mybooks.bookinfoservice.dto.BookInfoResponseDto;
import com.mybooks.bookinfoservice.exception.BookNotFoundException;
import com.mybooks.bookinfoservice.mapper.BookInfoMapper;
import com.mybooks.bookinfoservice.repository.BookInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class BookInfoServiceImpl implements BookInfoService {
    private final BookInfoRepository repository;
    private final BookInfoMapper mapper;
    private final RestTemplate restTemplate;

    @Override
    public BookInfoResponseDto getBookInfo(String id) {
        BookDto bookDto = repository.findById(id)
                .map(mapper::transferToBookDto)
                .orElseThrow(() -> new BookNotFoundException(id));

        String url = "http://localhost:8082/book/author/" + bookDto.getAuthorId();
        AuthorDto authorDto = restTemplate.getForObject(url, AuthorDto.class);

        return BookInfoResponseDto.builder()
                .id(bookDto.getId())
                .title(bookDto.getTitle())
                .author(authorDto)
                .build();
    }
}
