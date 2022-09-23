package com.mybooks.oauthservice.mapper;

import com.mybooks.oauthservice.model.User;
import com.mybooks.oauthservice.model.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity transferToUserEntity(User user);

    User transferToUser(UserEntity userEntity);
}
