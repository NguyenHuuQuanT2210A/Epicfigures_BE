package com.example.userservice.controllers;

import com.example.userservice.dtos.request.ProductRequest;
import com.example.userservice.services.WebClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Service
@RestController
@RequestMapping("/api/v1/web-client")
public class WebClientController {
    @Autowired
    private WebClientService webClientService;

    @GetMapping
    public Mono<String> getAllProducts() {
        return webClientService.getAllProducts();
    }

    @PostMapping
    public Mono<String> createProduct(@RequestBody ProductRequest product) {
        return webClientService.createProduct(product);
    }
}
