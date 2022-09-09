package com.mybooks.clientservice.mapper;

import com.mybooks.clientservice.model.User;
import com.mybooks.clientservice.model.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity transferToUserEntity(User user);

    User transferToUser(UserEntity userEntity);
}
