package com.mybooks.oauthserver.mapper;

import com.mybooks.oauthserver.dto.UserResponseDto;
import com.mybooks.oauthserver.model.User;
import com.mybooks.oauthserver.model.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserInfoMapper {
    UserEntity transferToUserEntity(User user);

    User transferToUser(UserEntity userEntity);

    UserResponseDto transferToUserResponseDto(UserEntity userEntity);
}
