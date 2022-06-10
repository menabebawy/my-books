package com.mybooks.api.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "aws")
public class AWSConfig {
    /**
     * Aws access key ID
     */
    private String accessKey;

    /**
     * Aws secret access key
     */
    private String secretKey;

    /**
     * Aws region
     */
    private String region;

    /**
     * dynamodb endpoint
     */
    private String endpoint;
}
