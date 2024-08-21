package com.example.inventoryservice.services;

import com.example.inventoryservice.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "http://localhost:8082/api/v1/products")
public interface ProductClients {
    @GetMapping("/{id}")
    ProductDTO getProductById(@PathVariable("id") Long id);
}
