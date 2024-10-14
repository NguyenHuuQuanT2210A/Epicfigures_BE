package com.example.couponservice.services;

import com.example.couponservice.config.AuthenticationRequestInterceptor;
import com.example.couponservice.dto.response.ApiResponse;
import com.example.couponservice.dto.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "productInventory-service", url = "http://localhost:8082/api/v1/products",
        configuration = { AuthenticationRequestInterceptor.class })
public interface ProductClients {
    @GetMapping("/id/{id}")
    ApiResponse<ProductResponse> getProductById(@PathVariable("id") Long id);

    @PutMapping("/updateStockQuantity/{id}")
    void updateStockQuantity(@PathVariable Long id, @RequestParam Integer quantity);
}
