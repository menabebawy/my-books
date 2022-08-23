package com.mybooks.bookinfoservice;

import com.mybooks.bookinfoservice.controller.BookInfoController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class BookInfoServiceApplicationTests {
    @Autowired
    private BookInfoController bookInfoController;

    @Test
    void contextLoads() {
        assertThat(bookInfoController).isNotNull();
    }
}
