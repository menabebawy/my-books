package com.mybooks.oauthservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class OAuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OAuthServiceApplication.class, args);
    }

}
