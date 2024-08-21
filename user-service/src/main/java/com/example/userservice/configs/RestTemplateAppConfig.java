package com.example.userservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateAppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}