package com.mybooks.api.mapper;

import com.mybooks.api.model.User;
import com.mybooks.api.model.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity transferToUserEntity(User user);

    User transferToUser(UserEntity userEntity);
}
