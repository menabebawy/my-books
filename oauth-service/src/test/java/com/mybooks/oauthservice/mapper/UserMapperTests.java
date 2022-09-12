package com.mybooks.oauthservice.mapper;

import com.mybooks.oauthservice.model.User;
import com.mybooks.oauthservice.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTests {
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        userMapper = new UserMapperImpl();
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void givenUser_mapToUserEntity_thenCorrect() {
        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .email("test@mailc.com")
                .password(passwordEncoder.encode("password")).build();

        UserEntity userEntity = userMapper.transferToUserEntity(user);

        assertEquals(userEntity.getEmail(), user.getEmail());
        assertEquals(userEntity.getId(), user.getId());
        assertEquals(userEntity.getPassword(), user.getPassword());
    }

    @Test
    void givenUserEntity_mapToUser_thenCorrect() {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@mail.com");
        userEntity.setPassword(passwordEncoder.encode("password"));
        userEntity.setId(UUID.randomUUID().toString());

        User user = userMapper.transferToUser(userEntity);

        assertEquals(userEntity.getEmail(), user.getEmail());
        assertEquals(userEntity.getId(), user.getId());
        assertEquals(userEntity.getPassword(), user.getPassword());
    }
}
