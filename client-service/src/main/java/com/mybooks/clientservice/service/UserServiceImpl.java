package com.mybooks.clientservice.service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import com.mybooks.clientservice.config.AwsProperties;
import com.mybooks.clientservice.dto.LoginRequestDto;
import com.mybooks.clientservice.dto.LoginResponseDto;
import com.mybooks.clientservice.exception.ServiceException;
import com.mybooks.clientservice.exception.UserPasswordResetRequiredException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.mybooks.clientservice.service.CognitoAttributesEnum.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final AWSCognitoIdentityProvider awsCognitoIdentityProvider;
    private final AwsProperties awsProperties;

    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        Map<String, String> authParams = new LinkedHashMap<>() {{
            put(USERNAME.name(), request.getUsername());
            put(PASSWORD.name(), request.getPassword());
            put(SECRET_HASH.name(),
                    calculateSecretHash(awsProperties.getCognito().getAppClientId(),
                            awsProperties.getCognito().getAppClientSecret(),
                            request.getUsername()));
        }};

        AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest()
                .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                .withUserPoolId(awsProperties.getCognito().getUserPoolId())
                .withClientId(awsProperties.getCognito().getAppClientId())
                .withAuthParameters(authParams);

        try {
            AdminInitiateAuthResult authResult = awsCognitoIdentityProvider.adminInitiateAuth(authRequest);
            AuthenticationResultType resultType = authResult.getAuthenticationResult();
            return LoginResponseDto.builder()
                    .accessToken(resultType.getAccessToken())
                    .refreshToken(resultType.getRefreshToken())
                    .idToken(resultType.getIdToken())
                    .expiresIn(resultType.getExpiresIn())
                    .tokenType(resultType.getTokenType())
                    .build();
        } catch (PasswordResetRequiredException e) {
            throw new UserPasswordResetRequiredException(request.getUsername());
        }
    }

    private String calculateSecretHash(String userPoolClientId, String userPoolClientSecret, String userName) {
        final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

        SecretKeySpec signingKey = new SecretKeySpec(
                userPoolClientSecret.getBytes(StandardCharsets.UTF_8),
                HMAC_SHA256_ALGORITHM);
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(signingKey);
            mac.update(userName.getBytes(StandardCharsets.UTF_8));
            byte[] rawHmac = mac.doFinal(userPoolClientId.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (Exception e) {
            throw new ServiceException("could not encode");
        }
    }
}
