package com.example.userservice.controllers;

import com.example.userservice.dtos.request.ProductRequest;
import com.example.userservice.services.FeignClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
@RestController
@RequestMapping("/api/v1/feign-client")
public class FeignClientController {
    @Autowired
    private FeignClientServiceImpl feignClientService;

    @GetMapping
    public String getAllProducts() {
        return feignClientService.getAllProducts();
    }

    @PostMapping
    public String createProduct(@RequestBody ProductRequest product) {
        return feignClientService.createProduct(product);
    }
}
