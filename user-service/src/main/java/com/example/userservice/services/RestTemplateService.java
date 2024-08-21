package com.example.userservice.services;

import com.example.userservice.dtos.request.ProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

@Service
public class RestTemplateService {
    @Autowired
    private RestTemplate restTemplate;

    public String getAllProducts() {
        String url = "http://localhost:8089/api/v1/products";
        return restTemplate.getForObject(url, String.class);
    }

    public String createProduct(ProductRequest product) {
        String url = "http://localhost:8089/api/v1/products";
        HttpEntity<ProductRequest> request = new HttpEntity<>(product);
        return restTemplate.postForObject(url, request, String.class);
    }
}
