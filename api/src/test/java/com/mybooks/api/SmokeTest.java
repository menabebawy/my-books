package com.mybooks.api;

import com.mybooks.api.controller.AuthorController;
import com.mybooks.api.controller.BookController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class SmokeTest {
    @Autowired
    private BookController bookController;
    @Autowired
    private AuthorController authorController;

    @Test
    void contextLoads() throws Exception {
        assertThat(bookController).isNotNull();
        assertThat(authorController).isNotNull();
    }
}
