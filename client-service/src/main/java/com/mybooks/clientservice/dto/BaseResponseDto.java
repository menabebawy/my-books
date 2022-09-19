package com.mybooks.clientservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BaseResponseDto {
    Object data;
    String message;
    Boolean error;

    public BaseResponseDto(Object data, String message) {
        this.data = data;
        this.message = message;
    }
}
