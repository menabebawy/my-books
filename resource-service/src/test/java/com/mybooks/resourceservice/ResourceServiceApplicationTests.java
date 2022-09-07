package com.mybooks.resourceservice;

import com.mybooks.resourceservice.controller.AuthorController;
import com.mybooks.resourceservice.controller.BookController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class ResourceServiceApplicationTests {
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
