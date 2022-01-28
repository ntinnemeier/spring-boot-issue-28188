package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class CompositeMeterApplication {
    private final HttpBinOrgClient httpBinOrgClient;

    @Autowired
    public CompositeMeterApplication(HttpBinOrgClient httpBinOrgClient) {
        this.httpBinOrgClient = httpBinOrgClient;
    }

    public static void main(String[] args) {
        SpringApplication.run(CompositeMeterApplication.class, args);
    }

    @Async()
    @Scheduled(fixedRate = 10L)
    public void sendHttpRequests() {
        httpBinOrgClient.sendGetRequest();
    }
}
