package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class HttpBinOrgClient {

    private final WebClient webClient;

    @Autowired
    HttpBinOrgClient(WebClient.Builder webClientBuilder) {
        webClient = webClientBuilder
                .baseUrl("httpbin.org")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public void sendGetRequest() {
        webClient
                .get()
                .uri(builder -> builder
                        .path("/response-headers")
                        .queryParam("key", ThreadLocalRandom.current().nextInt())
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}