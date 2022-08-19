package com.mybooks.authorinfoservice;

import com.mybooks.authorinfoservice.controller.AuthorController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class AuthorInfoServiceApplicationTests {

    @Autowired
    private AuthorController authorController;

    @Test
    void contextLoads() {
        assertThat(authorController).isNotNull();
    }

}
