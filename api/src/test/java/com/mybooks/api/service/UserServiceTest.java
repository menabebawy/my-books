package com.mybooks.api.service;

import com.mybooks.api.config.JwtEndpointAccessTokenGenerator;
import com.mybooks.api.config.JwtSecretKey;
import com.mybooks.api.dto.AuthenticationRequestDTO;
import com.mybooks.api.exception.InvalidLoginException;
import com.mybooks.api.exception.UserAlreadyExistException;
import com.mybooks.api.mapper.UserMapper;
import com.mybooks.api.model.User;
import com.mybooks.api.model.UserEntity;
import com.mybooks.api.model.UserRole;
import com.mybooks.api.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Mock
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtEndpointAccessTokenGenerator jwtEndpointAccessTokenGenerator;
    @Autowired
    JwtSecretKey jwtSecretKey;

    private final String GIVEN_EMAIL = "test@gmail.com";
    private final String GIVEN_PASSWORD = "PASSWORD123";
    private final String NOT_FOUND_EMAIL = "test@hotmail.com";

    @BeforeEach
    void setup() {
        userService = new UserServiceImpl(userRepository, passwordEncoder, userMapper, jwtEndpointAccessTokenGenerator, jwtSecretKey);
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
                .roles(Collections.singleton(UserRole.ROLE_USER))
                .build();
        UserEntity userEntity = userMapper.transferToUserEntity(user);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
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

    @Test
    void whenPostLoginUser_thenCorrectResponse() {
        AuthenticationRequestDTO requestDTO = AuthenticationRequestDTO.builder()
                .email(GIVEN_EMAIL)
                .password(GIVEN_PASSWORD)
                .build();
        User user = User.builder()
                .email(GIVEN_EMAIL)
                .password(passwordEncoder.encode(GIVEN_PASSWORD))
                .roles(Collections.singleton(UserRole.ROLE_USER))
                .build();
        when(userRepository.findByEmail(GIVEN_EMAIL)).thenReturn(Optional.of(userMapper.transferToUserEntity(user)));
        assertNotNull(userService.login(requestDTO).getAccessToken());
    }
}
