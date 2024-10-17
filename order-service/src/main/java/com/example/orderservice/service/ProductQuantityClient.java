package com.example.orderservice.service;

import com.example.orderservice.config.AuthenticationRequestInterceptor;
import com.example.orderservice.dto.request.ProductQuantityRequest;
import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.dto.response.ProductQuantityResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "product-quantity-inventory-service", url = "http://localhost:8082/api/v1/product_quantity",
        configuration = { AuthenticationRequestInterceptor.class })
public interface ProductQuantityClient {
    @GetMapping("/productId/{productId}")
    ApiResponse<ProductQuantityResponse> getProductQuantityByProductId(@PathVariable Long productId);

    @PutMapping("/{id}")
    void updateProductQuantity(@PathVariable Long id, @Valid @RequestBody ProductQuantityRequest request);
}
