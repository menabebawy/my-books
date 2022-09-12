package com.mybooks.clientservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/book")
@AllArgsConstructor
public class BookController {

    private final RestTemplate restTemplate;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookDTO> index(
            @RegisteredOAuth2AuthorizedClient("mybooks-client-oidc") OAuth2AuthorizedClient authorizedClient
    ) {
        String jwtAccessToken = authorizedClient.getAccessToken().getTokenValue();
        System.out.println("jwtAccessToken =  " + jwtAccessToken);

        String url = "http://127.0.0.1:8090/book";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtAccessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<List<BookDTO>> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<BookDTO>>() {
                });

        return responseEntity.getBody();
    }
}
