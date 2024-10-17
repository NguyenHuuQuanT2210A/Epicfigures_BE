package com.example.inventoryservice.services;

import com.example.inventoryservice.config.AuthenticationRequestInterceptor;
import com.example.inventoryservice.dto.request.ProductQuantityRequest;
import com.example.inventoryservice.dto.response.ApiResponse;
import com.example.inventoryservice.dto.response.ProductQuantityResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-quantity-inventory-service", url = "http://localhost:8082/api/v1/product_quantity",
        configuration = { AuthenticationRequestInterceptor.class })
public interface ProductQuantityClient {
    @GetMapping("/productId/{productId}")
    ApiResponse<ProductQuantityResponse> getProductQuantityByProductId(@PathVariable Long productId);

    @PutMapping("/{id}")
    void updateProductQuantity(@PathVariable Long id, @Valid @RequestBody ProductQuantityRequest request);
}
