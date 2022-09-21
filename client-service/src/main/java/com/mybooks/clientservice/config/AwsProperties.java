package com.mybooks.clientservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aws")
public class AwsProperties {
    private final Cognito cognito = new Cognito();

    @Data
    public static class Cognito {
        private String userPoolId;
        private String appClientId;
        private String appClientSecret;
    }
}
