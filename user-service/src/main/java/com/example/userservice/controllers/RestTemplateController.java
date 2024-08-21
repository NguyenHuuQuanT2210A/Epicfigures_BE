package com.example.userservice.controllers;

import com.example.userservice.dtos.request.ProductRequest;
import com.example.userservice.services.RestTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Service
@RestController
@RequestMapping("/api/v1/rest-template")
public class RestTemplateController {
    @Autowired
    private RestTemplateService restTemplateService;

    @GetMapping
    public String getAllProducts() {
        return restTemplateService.getAllProducts();
    }

    @PostMapping
    public String createProduct(@RequestBody ProductRequest product) {
        return restTemplateService.createProduct(product);
    }
}
