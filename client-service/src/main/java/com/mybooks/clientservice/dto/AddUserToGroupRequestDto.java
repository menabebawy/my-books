package com.mybooks.clientservice.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AddUserToGroupRequestDto {
    String username;
    String groupName;
}
