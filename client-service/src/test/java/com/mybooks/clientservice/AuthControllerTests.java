package com.mybooks.clientservice;

import com.amazonaws.services.cognitoidp.model.ExpiredCodeException;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybooks.clientservice.dto.*;
import com.mybooks.clientservice.exception.ServiceException;
import com.mybooks.clientservice.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthControllerTests {
    private final String baseUrl = "/user";
    private final String loginPath = "/login";
    private final String accessTokenPath = "/access-token";
    private final String changePasswordPath = "/change-password";
    private final String forgotPasswordPath = "/forgot-password";
    private final String confirmForgotPasswordPath = "/confirm-forgot-password";
    private final String signupPath = "/signup";
    private final String username = "test@test.com";
    private final String password = "password@123";
    private final String refreshToken = "refreshToken";
    private final String accessToken = "accessToken";

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    UserService userService;

    @Test
    void whenLogin_givenValidRequest_thenCorrect() throws Exception {
        LoginRequestDto loginRequest = LoginRequestDto.builder()
                .password(password)
                .username(username)
                .build();

        when(userService.login(loginRequest)).thenReturn(authenticatedResponse());

        mockMvc.perform(post(baseUrl + loginPath)
                        .contentType(APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.accessToken", Matchers.is(authenticatedResponse().getAccessToken())))
                .andExpect(jsonPath("$.refreshToken", Matchers.is(authenticatedResponse().getRefreshToken())))
                .andExpect(jsonPath("$.tokenType", Matchers.is(authenticatedResponse().getTokenType())));
    }

    @Test
    void whenLogin_givenIncorrectUsernameOrPassword_thenCorrect() throws Exception {
        LoginRequestDto loginRequest = LoginRequestDto.builder()
                .password(password)
                .username(username)
                .build();

        when(userService.login(loginRequest)).thenThrow(new NotAuthorizedException(""));

        mockMvc.perform(post(baseUrl + loginPath)
                        .contentType(APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.message", Matchers.is("Incorrect username or password.")));
    }

    @Test
    void whenAccessToken_givenValidRequest_thenCorrect() throws Exception {
        RefreshTokenRequestDto requestDto = RefreshTokenRequestDto.builder()
                .username(username)
                .refreshToken(refreshToken)
                .build();

        when(userService.accessToken(requestDto)).thenReturn(authenticatedResponse());

        mockMvc.perform(post(baseUrl + accessTokenPath)
                        .contentType(APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.accessToken", Matchers.is(authenticatedResponse().getAccessToken())))
                .andExpect(jsonPath("$.tokenType", Matchers.is(authenticatedResponse().getTokenType())));
    }

    @Test
    void whenAccessToken_givenInvalidUsername_thenCorrect() throws Exception {
        RefreshTokenRequestDto requestDto = RefreshTokenRequestDto.builder()
                .username(username)
                .refreshToken(refreshToken)
                .build();

        when(userService.accessToken(requestDto)).thenThrow(new NotAuthorizedException(""));

        mockMvc.perform(post(baseUrl + accessTokenPath)
                        .contentType(APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.message", Matchers.is("Incorrect username or password.")));
    }

    @Test
    void whenAccessToken_givenMissingUsername_thenCorrect() throws Exception {
        RefreshTokenRequestDto requestDto = RefreshTokenRequestDto.builder()
                .refreshToken(refreshToken)
                .build();

        when(userService.accessToken(requestDto)).thenThrow(new ServiceException(""));

        mockMvc.perform(post(baseUrl + accessTokenPath)
                        .contentType(APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.message", Matchers.is("Parameter is missing or invalid.")));
    }

    @Test
    void whenChangePassword_givenValidRequest_thenCorrect() throws Exception {
        when(userService.changePassword(changePasswordRequestDto()))
                .thenReturn(new MessageResponseDto("Password has been changed successfully."));

        mockMvc.perform(post(baseUrl + changePasswordPath)
                        .contentType(APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(changePasswordRequestDto())))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.message", Matchers.is("Password has been changed successfully.")));
    }

    @Test
    void whenChangePassword_givenInvalidAccessToken_thenCorrect() throws Exception {
        mockMvc.perform(post(baseUrl + changePasswordPath)
                        .contentType(APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(changePasswordPath)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.message", Matchers.is("Parameter is missing or invalid.")));
    }

    @Test
    void whenForgotPassword_givenValidRequest_thenCorrect() throws Exception {
        String message = String.format("You will get an email to %s soon.", username);

        when(userService.forgotPassword(username)).thenReturn(messageResponseDto(message));

        mockMvc.perform(get(baseUrl + forgotPasswordPath)
                        .contentType(APPLICATION_JSON)
                        .param("username", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.message", Matchers.is(message)));
    }

    @Test
    void whenForgotPassword_givenMissingUsername_thenCorrect() throws Exception {
        mockMvc.perform(get(baseUrl + forgotPasswordPath)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.message", Matchers.is("username is missing with type of String")));
    }

    @Test
    void whenConfirmForgotPassword_givenValidRequest_thenCorrect() throws Exception {
        String message = "done!, try to login now";

        when(userService.confirmForgotPassword(confirmForgotPasswordRequestDto()))
                .thenReturn(messageResponseDto(message));

        mockMvc.perform(post(baseUrl + confirmForgotPasswordPath)
                        .contentType(APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(confirmForgotPasswordRequestDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.message", Matchers.is(message)));
    }

    @Test
    void whenConfirmForgotPassword_givenInvalidVerificationCode_thenCorrect() throws Exception {
        String message = "Invalid verification code, please request a code again.";

        when(userService.confirmForgotPassword(confirmForgotPasswordRequestDto()))
                .thenThrow(new ExpiredCodeException(""));

        mockMvc.perform(post(baseUrl + confirmForgotPasswordPath)
                        .contentType(APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(confirmForgotPasswordRequestDto())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.message", Matchers.is(message)));
    }

    @Test
    void whenSignup_givenValidRequest_thenCorrect() throws Exception {
        SignupRequestDto requestDto = SignupRequestDto.builder()
                .email(username)
                .password(password)
                .firstName("Test")
                .lastName("Test")
                .phoneNumber("+4322002222")
                .roles(Collections.singleton("ROLE_ADMIN"))
                .build();

        String message = username + " is created successfully";

        when(userService.signup(requestDto)).thenReturn(messageResponseDto(message));

        mockMvc.perform(post(baseUrl + signupPath)
                        .contentType(APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.message", Matchers.is(message)));
    }

    @Test
    void whenSignup_givenMissingPasswordValue_thenCorrect() throws Exception {
        SignupRequestDto requestDto = SignupRequestDto.builder()
                .email(username)
                .firstName("Test")
                .lastName("Test")
                .phoneNumber("+4322002222")
                .roles(Collections.singleton("ROLE_ADMIN"))
                .build();

        mockMvc.perform(post(baseUrl + signupPath)
                        .contentType(APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.message", Matchers.is("Parameter is missing or invalid.")));
    }

    private ConfirmForgotPasswordRequestDto confirmForgotPasswordRequestDto() {
        String confirmationCode = "Code123";
        return ConfirmForgotPasswordRequestDto.builder()
                .confirmationCode(confirmationCode)
                .username(username)
                .password(password)
                .passwordConfirm(password)
                .build();
    }

    private MessageResponseDto messageResponseDto(String message) {
        return MessageResponseDto.builder().message(message).build();
    }

    private AuthenticatedResponseDto authenticatedResponse() {
        return AuthenticatedResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(3000)
                .tokenType("Bearer")
                .build();
    }

    private ChangePasswordRequestDto changePasswordRequestDto() {
        return ChangePasswordRequestDto.builder()
                .proposedPassword("password@1234")
                .previousPassword(password)
                .accessToken(accessToken)
                .build();

    }
}
