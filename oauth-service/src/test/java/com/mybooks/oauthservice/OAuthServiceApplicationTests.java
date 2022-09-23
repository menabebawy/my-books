package com.mybooks.oauthservice;

import com.mybooks.oauthservice.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class OAuthServiceApplicationTests {
    @Autowired
    UserController userController;

    @Test
    void contextLoads() {
        assertThat(userController).isNotNull();
    }

}
