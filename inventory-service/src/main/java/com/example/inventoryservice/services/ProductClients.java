package com.example.inventoryservice.services;

import com.example.inventoryservice.config.AuthenticationRequestInterceptor;
import com.example.inventoryservice.dto.ApiResponse;
import com.example.inventoryservice.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "productInventory-service", url = "http://localhost:8082/api/v1/products",
        configuration = { AuthenticationRequestInterceptor.class })
public interface ProductClients {
    @GetMapping("/id/{id}")
    ApiResponse<ProductDTO> getProductById(@PathVariable("id") Long id);

    @PutMapping("/updateQuantity/{id}")
    void updateStockQuantity(@PathVariable Long id, @RequestParam Integer quantity);
}
