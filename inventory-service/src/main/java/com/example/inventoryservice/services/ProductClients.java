package com.example.inventoryservice.services;

import com.example.inventoryservice.config.AuthenticationRequestInterceptor;
import com.example.inventoryservice.dto.request.ProductQuantityRequest;
import com.example.inventoryservice.dto.response.ApiResponse;
import com.example.inventoryservice.dto.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "productInventory-service", url = "http://localhost:8082/api/v1/products",
        configuration = { AuthenticationRequestInterceptor.class })
public interface ProductClients {
    @GetMapping("/id/{id}")
    ApiResponse<ProductResponse> getProductById(@PathVariable("id") Long id);

//    @PutMapping("/updateStockQuantity/{id}")
//    void updateStockQuantity(@PathVariable Long id, @RequestParam Integer quantity);

    @PutMapping("/updateQuantity/{id}")
    void updateQuantity(@PathVariable Long id, @RequestBody ProductQuantityRequest request);
}
