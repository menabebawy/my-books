package com.mybooks.clientservice.service;

import com.amazonaws.services.cognitoidp.model.*;
import com.mybooks.clientservice.dto.*;
import com.mybooks.clientservice.service.awscognito.AwsCognitoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTests {
    @Mock
    AwsCognitoService awsCognitoService;

    private UserService userService;

    @BeforeEach
    void setup() {
        userService = new UserServiceImpl(awsCognitoService);
    }

    @AfterEach
    void destroy() {
        userService = null;
    }

    @Test
    void whenLogin_givenValidRequest_thenCorrect() {
        LoginRequestDto loginRequest = LoginRequestDto.builder()
                .username("username")
                .password("password").build();

        AuthenticatedResponseDto responseDto = AuthenticatedResponseDto.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .tokenType("Bearer")
                .expiresIn(3000)
                .build();

        when(awsCognitoService.login(loginRequest)).thenReturn(Optional.ofNullable(responseDto));

        assert responseDto != null;
        assertEquals(userService.login(loginRequest).getAccessToken(), responseDto.getAccessToken());
        assertEquals(userService.login(loginRequest).getRefreshToken(), responseDto.getRefreshToken());
        assertEquals(userService.login(loginRequest).getTokenType(), responseDto.getTokenType());
    }

    @Test
    void whenLoginNeedsPasswordResetRequired_givenValidRequest_thenCorrect() {
        LoginRequestDto loginRequest = LoginRequestDto.builder()
                .username("username")
                .password("password").build();

        when(awsCognitoService.login(loginRequest)).thenThrow(new PasswordResetRequiredException(""));

        assertThrows(PasswordResetRequiredException.class, () -> userService.login(loginRequest));
        verify(awsCognitoService, times(1)).login(loginRequest);
    }

    @Test
    void whenLogin_givenInvalidRequest_thenCorrect() {
        LoginRequestDto loginRequest = LoginRequestDto.builder()
                .password("password")
                .build();

        when(awsCognitoService.login(loginRequest)).thenThrow(new InvalidParameterException());

        assertThrows(InvalidParameterException.class, () -> userService.login(loginRequest));
        verify(awsCognitoService, times(1)).login(loginRequest);
    }

    @Test
    void whenForgotPassword_givenValidRequest_thenCorrect() {
        String username = "test@test.com";
        String message = String.format("You will get an email to %s soon.", username);

        when(awsCognitoService.forgotPassword(username)).thenReturn(Optional.of(username));

        assertEquals(userService.forgotPassword(username).getMessage(), message);
    }

    @Test
    void whenForgotPassword_givenInvalidRequest_thenCorrect() {
        when(awsCognitoService.forgotPassword(null)).thenThrow(new InvalidParameterException());

        assertThrows(InvalidParameterException.class, () -> userService.forgotPassword(null));
        verify(awsCognitoService, times(1)).forgotPassword(null);
    }

    @Test
    void whenForgotPassword_givenExceededRequestLimit_thenCorrect() {
        String username = "test@test.com";
        when(awsCognitoService.forgotPassword(username)).thenThrow(new LimitExceededException(""));

        assertThrows(LimitExceededException.class, () -> userService.forgotPassword(username));
        verify(awsCognitoService, times(1)).forgotPassword(username);
    }

    @Test
    void whenConfirmForgotPassword_givenValidRequest_thenCorrect() {
        ConfirmForgotPasswordRequestDto requestDto = ConfirmForgotPasswordRequestDto.builder()
                .confirmationCode("123456")
                .password("password123")
                .confirmationCode("password123").build();

        ConfirmForgotPasswordResult result = new ConfirmForgotPasswordResult();

        when(awsCognitoService.confirmForgotPassword(requestDto)).thenReturn(Optional.of(result));

        assertNotNull(userService.confirmForgotPassword(requestDto));
        verify(awsCognitoService, times(1)).confirmForgotPassword(requestDto);
    }

    @Test
    void whenConfirmForgotPassword_givenRequestWithExpiredCode_thenCorrect() {
        ConfirmForgotPasswordRequestDto requestDto = ConfirmForgotPasswordRequestDto.builder()
                .confirmationCode("123456")
                .password("password123")
                .confirmationCode("password123").build();

        when(awsCognitoService.confirmForgotPassword(requestDto)).thenThrow(new ExpiredCodeException(""));

        assertThrows(ExpiredCodeException.class, () -> userService.confirmForgotPassword(requestDto));
        verify(awsCognitoService, times(1)).confirmForgotPassword(requestDto);
    }

    @Test
    void whenConfirmForgotPassword_givenUnmatchedPasswords_thenCorrect() {
        ConfirmForgotPasswordRequestDto requestDto = ConfirmForgotPasswordRequestDto.builder()
                .confirmationCode("123456")
                .password("password1234")
                .confirmationCode("password123").build();

        when(awsCognitoService.confirmForgotPassword(requestDto)).thenThrow(new InvalidParameterException());

        assertThrows(InvalidParameterException.class, () -> userService.confirmForgotPassword(requestDto));
        verify(awsCognitoService, times(1)).confirmForgotPassword(requestDto);
    }

    @Test
    void whenAccessToken_givenValidRequest_thenCorrect() {
        RefreshTokenRequestDto requestDto = RefreshTokenRequestDto.builder()
                .refreshToken("refreshToken")
                .username("test@test.com")
                .build();

        AuthenticatedResponseDto responseDto = AuthenticatedResponseDto.builder()
                .accessToken("accessToken")
                .tokenType("Bearer")
                .expiresIn(3000)
                .build();

        when(awsCognitoService.accessToken(requestDto)).thenReturn(Optional.of(responseDto));

        assertNotNull(userService.accessToken(requestDto));
        assertEquals(userService.accessToken(requestDto).getAccessToken(), responseDto.getAccessToken());
        assertEquals(userService.accessToken(requestDto).getTokenType(), responseDto.getTokenType());
        assertEquals(userService.accessToken(requestDto).getExpiresIn(), responseDto.getExpiresIn());
    }

    @Test
    void whenAccessToken_givenInvalidRequest_thenCorrect() {
        RefreshTokenRequestDto requestDto = RefreshTokenRequestDto.builder()
                .username("test@test.com")
                .build();

        when(awsCognitoService.accessToken(requestDto)).thenThrow(new InvalidParameterException());

        assertThrows(InvalidParameterException.class, () -> userService.accessToken(requestDto));
        verify(awsCognitoService, times(1)).accessToken(requestDto);
    }

    @Test
    void whenChangePassword_givenValidRequest_thenCorrect() {
        ChangePasswordRequestDto requestDto = ChangePasswordRequestDto.builder()
                .accessToken("accessToken")
                .previousPassword("password123")
                .proposedPassword("password123")
                .build();

        when(awsCognitoService.changePassword(requestDto)).thenReturn(Optional.of(new ChangePasswordResult()));

        assertNotNull(userService.changePassword(requestDto));
        verify(awsCognitoService, times(1)).changePassword(requestDto);
    }

    @Test
    void whenChangePassword_givenInvalidRequest_thenCorrect() {
        ChangePasswordRequestDto requestDto = ChangePasswordRequestDto.builder()
                .accessToken("accessToken")
                .previousPassword("password1234")
                .proposedPassword("password123")
                .build();

        when(awsCognitoService.changePassword(requestDto)).thenThrow(new InvalidPasswordException(""));

        assertThrows(InvalidPasswordException.class, () -> userService.changePassword(requestDto));
        verify(awsCognitoService, times(1)).changePassword(requestDto);
    }

    @Test
    void whenSignup_givenValidRequest_thenCorrect() {
        String email = "test@test.com";

        SignupRequestDto requestDto = SignupRequestDto.builder()
                .email(email)
                .firstName("testFirstName")
                .lastName("testLastName")
                .password("password123")
                .phoneNumber("+43666222000")
                .roles(Collections.singleton("ROLE_ADMIN"))
                .build();

        UserType user = new UserType();
        user.setUsername(email);
        user.setEnabled(true);

        when(awsCognitoService.signup(requestDto)).thenReturn(Optional.of(user));

        String message = String.format("%s is created successfully", email);

        assertEquals(userService.signup(requestDto).getMessage(), message);
        verify(awsCognitoService, times(1)).signup(requestDto);
    }
}
