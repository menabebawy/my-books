package com.mybooks.clientservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class ConfirmForgotPasswordResponseDto {
    String message;


}
