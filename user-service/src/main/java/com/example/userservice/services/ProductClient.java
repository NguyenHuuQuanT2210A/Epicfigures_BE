package com.example.userservice.services;

import com.example.userservice.dtos.response.ApiResponse;
import com.example.userservice.dtos.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cart-service", url = "http://localhost:8082/api/v1/products")
public interface ProductClient {
    @GetMapping("/{id}")
    ApiResponse<ProductResponse> getProductById(@PathVariable("id") Long id);
}
