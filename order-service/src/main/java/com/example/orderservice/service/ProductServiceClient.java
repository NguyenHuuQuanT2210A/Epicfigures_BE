package com.example.orderservice.service;

import com.example.common.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

@FeignClient(name = "product-service", url = "http://localhost:8082/api/v1/products")
public interface ProductServiceClient {

    @GetMapping("/{id}")
    ProductDTO getProductById(@PathVariable("id") Long id);

    @PostMapping("/list")
    List<ProductDTO> getProductsByIds(@RequestBody Set<Long> productIds);
}
