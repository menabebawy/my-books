package com.mybooks.oauthservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybooks.oauthservice.dto.AuthenticationRequestDTO;
import com.mybooks.oauthservice.exception.UserAlreadyExistException;
import com.mybooks.oauthservice.mapper.UserMapper;
import com.mybooks.oauthservice.mapper.UserMapperImpl;
import com.mybooks.oauthservice.model.User;
import com.mybooks.oauthservice.model.UserRole;
import com.mybooks.oauthservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WithMockUser
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserControllerTest {
    private final String BASE_URL = "/user";
    private final String GIVEN_EMAIL = "test@gmail.com";
    private final String GIVEN_PASSWORD = "PASSWORD123";
    private final String INVALID_EMAIL = "test.com";
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    UserService userService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapperImpl();
    }

    @Test
    void whenSignupUser_withValidRequestBody_thenCorrectResponse() throws Exception {
        AuthenticationRequestDTO authenticationRequestDTO = AuthenticationRequestDTO.builder()
                .email(GIVEN_EMAIL)
                .password(GIVEN_PASSWORD)
                .build();

        User user = User.builder()
                .email(GIVEN_EMAIL)
                .password(passwordEncoder.encode(GIVEN_PASSWORD))
                .roles(Collections.singleton(UserRole.USER))
                .build();

        when(userService.addUser(authenticationRequestDTO)).thenReturn(user);

        mockMvc.perform(post(BASE_URL + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(authenticationRequestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void whenSignupUser_withAlreadyExistUser_thenCorrectResponse() throws Exception {
        AuthenticationRequestDTO authenticationRequestDTO = AuthenticationRequestDTO.builder()
                .email(GIVEN_EMAIL)
                .password(GIVEN_PASSWORD)
                .build();

        when(userService.addUser(authenticationRequestDTO)).thenThrow(new UserAlreadyExistException(""));

        mockMvc.perform(post(BASE_URL + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(authenticationRequestDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    void whenSignupUser_withInvalidEmail_thenCorrectResponse() throws Exception {
        AuthenticationRequestDTO authenticationRequestDTO = AuthenticationRequestDTO.builder()
                .email(INVALID_EMAIL)
                .password(GIVEN_PASSWORD)
                .build();

        mockMvc.perform(post(BASE_URL + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(authenticationRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenSignupUser_withInvalidPassword_thenCorrectResponse() throws Exception {
        String INVALID_PASSWORD = "TEST";
        AuthenticationRequestDTO authenticationRequestDTO = AuthenticationRequestDTO.builder()
                .email(GIVEN_EMAIL)
                .password(INVALID_PASSWORD)
                .build();

        mockMvc.perform(post(BASE_URL + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(authenticationRequestDTO)))
                .andExpect(status().isBadRequest());
    }
}
