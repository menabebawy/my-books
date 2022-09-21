package com.mybooks.clientservice.service.awscognito;

import com.amazonaws.services.cognitoidp.model.AdminAddUserToGroupResult;
import com.amazonaws.services.cognitoidp.model.ConfirmForgotPasswordResult;
import com.amazonaws.services.cognitoidp.model.RevokeTokenResult;
import com.amazonaws.services.cognitoidp.model.UserType;
import com.mybooks.clientservice.dto.*;

import java.util.Optional;

public interface AwsCognitoService {
    Optional<AuthenticatedResponseDto> login(LoginRequestDto request);

    Optional<String> forgotPassword(String username);

    Optional<ConfirmForgotPasswordResult> confirmForgotPassword(ConfirmForgotPasswordRequestDto request);

    Optional<RevokeTokenResult> revokeToken(String token);

    Optional<AdminAddUserToGroupResult> addUserToGroup(AddUserToGroupRequestDto request);

    Optional<UserType> signup(SignupRequestDto request);
}
