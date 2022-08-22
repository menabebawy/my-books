package com.mybooks.bookratingservice;

import com.mybooks.bookratingservice.controller.BookRatingController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class BookRatingServiceApplicationTests {
    @Autowired
    private BookRatingController bookRatingController;

    @Test
    void contextLoads() {
        assertThat(bookRatingController).isNotNull();
    }

}
