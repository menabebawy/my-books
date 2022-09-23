package com.mybooks.clientservice;

import com.mybooks.clientservice.controller.AuthController;
import com.mybooks.clientservice.service.UserService;
import com.mybooks.clientservice.service.awscognito.AwsCognitoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ClientServiceApplicationTests {
    @Autowired
    private AuthController authController;

    @Autowired
    private UserService userService;

    @Autowired
    private AwsCognitoService awsCognitoService;

    @Test
    void contextLoads() {
        assertThat(authController).isNotNull();
        assertThat(userService).isNotNull();
        assertThat(awsCognitoService).isNotNull();
    }
}
