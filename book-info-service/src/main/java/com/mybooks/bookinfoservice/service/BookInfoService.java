package com.mybooks.bookinfoservice.service;

import com.mybooks.bookinfoservice.dto.BookInfoResponseDto;

public interface BookInfoService {
    BookInfoResponseDto getBookInfo(String id);
}
