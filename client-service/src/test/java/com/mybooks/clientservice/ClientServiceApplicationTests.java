package com.mybooks.clientservice;

import com.mybooks.clientservice.controller.AuthController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ClientServiceApplicationTests {
    @Autowired
    private AuthController authController;

    @Test
    void contextLoads() {
        assertThat(authController).isNotNull();
    }

}
