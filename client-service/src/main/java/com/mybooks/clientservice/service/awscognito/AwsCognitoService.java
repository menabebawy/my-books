package com.mybooks.clientservice.service.awscognito;

import com.amazonaws.services.cognitoidp.model.*;
import com.mybooks.clientservice.dto.*;

import java.util.Optional;

public interface AwsCognitoService {
    Optional<AuthenticatedResponseDto> login(LoginRequestDto request);

    Optional<String> forgotPassword(String username);

    Optional<ConfirmForgotPasswordResult> confirmForgotPassword(ConfirmForgotPasswordRequestDto request);

    Optional<RevokeTokenResult> revokeToken(String token);

    Optional<AdminAddUserToGroupResult> addUserToGroup(AddUserToGroupRequestDto request);

    Optional<UserType> signup(SignupRequestDto request);

    Optional<ChangePasswordResult> changePassword(ChangePasswordRequestDto request);
}
