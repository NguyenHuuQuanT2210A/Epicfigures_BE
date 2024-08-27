package com.example.productservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
//@Bean
//public RestTemplate restTemplate(RestTemplateBuilder builder) {
//    return builder.messageConverters(new MappingJackson2HttpMessageConverter()).build();
//}
}
