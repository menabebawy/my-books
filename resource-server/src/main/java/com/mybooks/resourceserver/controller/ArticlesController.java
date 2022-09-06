package com.mybooks.resourceserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticlesController {

    @GetMapping("/hello")
    public String getArticles() {
        return "hello";
    }
}
