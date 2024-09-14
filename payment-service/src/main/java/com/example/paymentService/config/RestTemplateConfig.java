package com.example.paymentService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.setInterceptors(Collections.singletonList((request, body, execution) -> {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (requestAttributes != null) {
                String authHeader = requestAttributes.getRequest().getHeader("Authorization");

                if (authHeader != null && !authHeader.isEmpty()) {
                    request.getHeaders().set("Authorization", authHeader);
                }
            }

            return execution.execute(request, body);
        }));
        return restTemplate;
    }

//@Bean
//public RestTemplate restTemplate(RestTemplateBuilder builder) {
//    return builder.messageConverters(new MappingJackson2HttpMessageConverter()).build();
//}
}
