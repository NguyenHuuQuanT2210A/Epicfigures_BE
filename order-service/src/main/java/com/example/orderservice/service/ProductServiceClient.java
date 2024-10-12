package com.example.orderservice.service;

import com.example.orderservice.config.AuthenticationRequestInterceptor;
import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.dto.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@FeignClient(name = "product-service", url = "http://localhost:8082/api/v1/products",
        configuration = { AuthenticationRequestInterceptor.class })
public interface ProductServiceClient {

    @GetMapping("/id/{id}")
    ApiResponse<ProductResponse> getProductById(@PathVariable("id") Long id);

    @PostMapping("/list")
    ApiResponse<List<ProductResponse>> getProductsByIds(@RequestBody Set<Long> productIds);

    @PutMapping("/updateSoldQuantity/{id}")
    void updateSoldQuantity(@PathVariable Long id, @RequestParam Integer quantity);
}
