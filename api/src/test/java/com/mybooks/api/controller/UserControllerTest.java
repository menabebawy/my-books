package com.mybooks.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybooks.api.dto.AuthenticationRequestDTO;
import com.mybooks.api.dto.TokenResponseDTO;
import com.mybooks.api.exception.UserAlreadyExistException;
import com.mybooks.api.mapper.UserMapper;
import com.mybooks.api.mapper.UserMapperImpl;
import com.mybooks.api.model.User;
import com.mybooks.api.model.UserRole;
import com.mybooks.api.service.UserService;
import org.hamcrest.Matchers;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WithMockUser
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserControllerTest {
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

    private final String BASE_URL = "/user";
    private final String GIVEN_EMAIL = "test@gmail.com";
    private final String GIVEN_PASSWORD = "PASSWORD123";
    private final String INVALID_EMAIL = "test.com";

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
                .roles(Collections.singleton(UserRole.ROLE_USER))
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

    @Test
    void whenLoginUser_withValidRequest_thenCorrectResponse() throws Exception {
        AuthenticationRequestDTO requestDTO = AuthenticationRequestDTO.builder()
                .email(GIVEN_EMAIL)
                .password(GIVEN_PASSWORD)
                .build();

        final String ACCESS_TOKEN = "ACCESS-TOKEN%%%$$$$";
        final String REFRESH_TOKEN = "REFRESH_TOKEN####§12";

        TokenResponseDTO tokenResponseDTO = TokenResponseDTO.builder()
                .accessToken(ACCESS_TOKEN)
                .refreshToken(REFRESH_TOKEN)
                .build();

        when(userService.login(requestDTO)).thenReturn(tokenResponseDTO);

        mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.accessToken", Matchers.is(ACCESS_TOKEN)))
                .andExpect(jsonPath("$.refreshToken", Matchers.is(REFRESH_TOKEN)));
    }

    @Test
    void whenPostLoginUser_withInvalidEmail_thenCorrectRequest() throws Exception {
        AuthenticationRequestDTO requestDTO = AuthenticationRequestDTO.builder()
                .email(INVALID_EMAIL)
                .password(GIVEN_PASSWORD)
                .build();

        mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPostLoginUser_withNullPassword_thenCorrectRequest() throws Exception {
        AuthenticationRequestDTO requestDTO = AuthenticationRequestDTO.builder()
                .email(GIVEN_EMAIL)
                .build();

        mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }
}
