package com.example.orderservice.service;

import com.example.common.dto.UserDTO;
import com.example.orderservice.dto.response.InventoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "inventory-service", url = "http://localhost:8888/api/v1/inventory")
public interface InventoryClient {
//    @GetMapping("/product/{productId}")
//    InventoryResponse getInventoryByProductId(@PathVariable("productId") Long productId);
}
