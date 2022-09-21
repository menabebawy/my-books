package com.mybooks.clientservice.service.awscognito;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import com.mybooks.clientservice.config.AwsProperties;
import com.mybooks.clientservice.dto.*;
import com.mybooks.clientservice.exception.ServiceException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static com.amazonaws.services.cognitoidp.model.DeliveryMediumType.EMAIL;
import static com.amazonaws.services.cognitoidp.model.MessageActionType.SUPPRESS;
import static com.mybooks.clientservice.service.awscognito.CognitoAttributesEnum.*;

@AllArgsConstructor
@Service
public class AwsCognitoServiceImpl implements AwsCognitoService {
    private final AWSCognitoIdentityProvider awsCognitoIdentityProvider;
    private final AwsProperties awsProperties;

    public Optional<UserType> signup(SignupRequestDto request) {
        AdminCreateUserRequest signUpRequest = new AdminCreateUserRequest()
                .withUserPoolId(awsProperties.getCognito().getUserPoolId())
                .withTemporaryPassword(request.getPassword())
                .withDesiredDeliveryMediums(EMAIL)
                .withUsername(request.getEmail())
                .withMessageAction(SUPPRESS)
                .withUserAttributes(
                        new AttributeType().withName("name").withValue(request.getFirstName()),
                        new AttributeType().withName("family_name").withValue(request.getLastName()),
                        new AttributeType().withName("email").withValue(request.getEmail()),
                        new AttributeType().withName("phone_number").withValue(request.getPhoneNumber()));

        AdminCreateUserResult createUserResult = awsCognitoIdentityProvider.adminCreateUser(signUpRequest);

        request.getRoles().forEach(role -> addUserToGroup(AddUserToGroupRequestDto.builder()
                .username(request.getEmail())
                .groupName(role).build()));

        setUserPassword(request.getEmail(), request.getPassword());

        UserType userType = createUserResult.getUser();

        return Optional.of(createUserResult.getUser());
    }

    private void setUserPassword(String username, String password) {
        AdminSetUserPasswordRequest adminSetUserPasswordRequest = new AdminSetUserPasswordRequest()
                .withUsername(username)
                .withPassword(password)
                .withUserPoolId(awsProperties.getCognito().getUserPoolId())
                .withPermanent(true);

        awsCognitoIdentityProvider.adminSetUserPassword(adminSetUserPasswordRequest);
    }

    public Optional<AdminAddUserToGroupResult> addUserToGroup(AddUserToGroupRequestDto request) {
        AdminAddUserToGroupRequest addUserToGroupRequest = new AdminAddUserToGroupRequest()
                .withUserPoolId(awsProperties.getCognito().getUserPoolId())
                .withGroupName(request.getGroupName())
                .withUsername(request.getUsername());

        return Optional.of(awsCognitoIdentityProvider.adminAddUserToGroup(addUserToGroupRequest));
    }

    public Optional<AuthenticatedResponseDto> login(LoginRequestDto request) {
        Map<String, String> authParams = new LinkedHashMap<>() {{
            put(USERNAME.name(), request.getUsername());
            put(PASSWORD.name(), request.getPassword());
            put(SECRET_HASH.name(), calculateSecretHash(request.getUsername()));
        }};

        AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest()
                .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                .withUserPoolId(awsProperties.getCognito().getUserPoolId())
                .withClientId(awsProperties.getCognito().getAppClientId())
                .withAuthParameters(authParams);

        AdminInitiateAuthResult authResult = awsCognitoIdentityProvider.adminInitiateAuth(authRequest);
        AuthenticationResultType resultType = authResult.getAuthenticationResult();
        return Optional.of(AuthenticatedResponseDto.builder()
                .accessToken(resultType.getAccessToken())
                .refreshToken(resultType.getRefreshToken())
                .idToken(resultType.getIdToken())
                .expiresIn(resultType.getExpiresIn())
                .tokenType(resultType.getTokenType())
                .build());
    }

    public Optional<String> forgotPassword(String username) {
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.withClientId(awsProperties.getCognito().getAppClientId())
                .withUsername(username)
                .withSecretHash(calculateSecretHash(username));

        ForgotPasswordResult result = awsCognitoIdentityProvider.forgotPassword(request);

        return Optional.of(result.getCodeDeliveryDetails().getDestination());
    }

    public Optional<ConfirmForgotPasswordResult> confirmForgotPassword(ConfirmForgotPasswordRequestDto request) {
        ConfirmForgotPasswordRequest confirmForgotPasswordRequest = new ConfirmForgotPasswordRequest();
        confirmForgotPasswordRequest.withClientId(awsProperties.getCognito().getAppClientId())
                .withUsername(request.getUsername())
                .withPassword(request.getPassword())
                .withConfirmationCode(request.getConfirmationCode())
                .withSecretHash(calculateSecretHash(request.getUsername()));
        return Optional.of(awsCognitoIdentityProvider.confirmForgotPassword(confirmForgotPasswordRequest));
    }

    public Optional<RevokeTokenResult> revokeToken(String token) {
        RevokeTokenRequest revokeTokenRequest = new RevokeTokenRequest();
        revokeTokenRequest.withClientId(awsProperties.getCognito().getAppClientId())
                .withClientSecret(awsProperties.getCognito().getAppClientSecret())
                .withToken(token);
        return Optional.of(awsCognitoIdentityProvider.revokeToken(revokeTokenRequest));
    }

    private String calculateSecretHash(String userName) {
        final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

        SecretKeySpec signingKey = new SecretKeySpec(
                awsProperties.getCognito().getAppClientSecret().getBytes(StandardCharsets.UTF_8),
                HMAC_SHA256_ALGORITHM);
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(signingKey);
            mac.update(userName.getBytes(StandardCharsets.UTF_8));
            byte[] rawHmac = mac.doFinal(awsProperties.getCognito().getAppClientId().getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (Exception e) {
            throw new ServiceException("could not encode");
        }
    }
}
