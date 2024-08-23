package com.example.userservice.services;

import com.example.userservice.dtos.request.ProductRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "product-service", url = "http://localhost:8082/api/v1/products")
public interface FeignClientService {

    @GetMapping
    String getAllProducts();

    @GetMapping("/{id}")
    ProductRequest getProductById(@PathVariable("id") Long id);

    @PostMapping()
    String createProduct(ProductRequest product);
}