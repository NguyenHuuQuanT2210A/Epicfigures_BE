package com.example.orderservice.service;

import com.example.orderservice.config.AuthenticationRequestInterceptor;
import com.example.orderservice.dto.request.InventoryRequest;
import com.example.orderservice.dto.request.ProductQuantityRequest;
import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.dto.response.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@FeignClient(name = "inventory-service", url = "http://localhost:8888/api/v1/inventory",
        configuration = { AuthenticationRequestInterceptor.class })
public interface InventoryServiceClient {
    @PostMapping
    ApiResponse<Long> createInventory(@RequestBody @Valid InventoryRequest request);
}
