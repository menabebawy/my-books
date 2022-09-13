package com.mybooks.oauthservice.service;

import com.mybooks.oauthservice.dto.AuthenticationRequestDTO;
import com.mybooks.oauthservice.exception.InvalidLoginException;
import com.mybooks.oauthservice.exception.UserAlreadyExistException;
import com.mybooks.oauthservice.mapper.UserMapper;
import com.mybooks.oauthservice.mapper.UserMapperImpl;
import com.mybooks.oauthservice.model.User;
import com.mybooks.oauthservice.model.UserEntity;
import com.mybooks.oauthservice.model.UserRole;
import com.mybooks.oauthservice.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {
    private final String GIVEN_EMAIL = "test@gmail.com";
    private final String GIVEN_PASSWORD = "PASSWORD123";
    private final String NOT_FOUND_EMAIL = "test@hotmail.com";
    @Mock
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    private UserService userService;
    private UserMapper userMapper;

    @BeforeEach
    void setup() {
        userMapper = new UserMapperImpl();
        userService = new UserServiceImpl(userRepository, passwordEncoder, userMapper);
    }

    @AfterEach
    void destroyAll() {
        userRepository.deleteAll();
    }

    @Test
    void whenGetUserByEmail_UserFound_thenCorrectResponse() {
        User user = User.builder()
                .email(GIVEN_EMAIL)
                .password(passwordEncoder.encode(GIVEN_PASSWORD))
                .build();
        when(userRepository.findByEmail(GIVEN_EMAIL)).thenReturn(Optional.of(userMapper.transferToUserEntity(user)));
        UserDetails userDetails = userService.loadUserByUsername(GIVEN_EMAIL);
        assertEquals(userDetails.getUsername(), user.getUsername());
    }

    @Test
    void whenGetUserByEmail_UserNotFound_thenCorrectResponse() {
        when(userRepository.findByEmail(NOT_FOUND_EMAIL)).thenThrow(new InvalidLoginException());
        assertThrows(InvalidLoginException.class, () -> userService.loadUserByUsername(NOT_FOUND_EMAIL));
        verify(userRepository, Mockito.times(1)).findByEmail(NOT_FOUND_EMAIL);
    }

    @Test
    void whenPostAddUser_thenCorrectResponse() {
        AuthenticationRequestDTO authenticationRequestDTO = AuthenticationRequestDTO.builder()
                .email(GIVEN_EMAIL)
                .password(GIVEN_PASSWORD)
                .build();
        User user = User.builder()
                .email(GIVEN_EMAIL)
                .password(passwordEncoder.encode(GIVEN_PASSWORD))
                .roles(Collections.singleton(UserRole.USER))
                .build();
        UserEntity userEntity = userMapper.transferToUserEntity(user);
        when(userRepository.save(ArgumentMatchers.any(UserEntity.class))).thenReturn(userEntity);
        User savedUser = userService.addUser(authenticationRequestDTO);
        assertEquals(savedUser.getUsername(), authenticationRequestDTO.getEmail());
    }

    @Test
    void whenPostAddUser_forFoundUser_thenCorrectResponse() {
        AuthenticationRequestDTO authenticationRequestDTO = AuthenticationRequestDTO.builder()
                .email(GIVEN_EMAIL)
                .password(GIVEN_PASSWORD)
                .build();
        when(userRepository.findByEmail(GIVEN_EMAIL)).thenThrow(new UserAlreadyExistException(""));
        assertThrows(UserAlreadyExistException.class, () -> userService.addUser(authenticationRequestDTO));
    }

}
