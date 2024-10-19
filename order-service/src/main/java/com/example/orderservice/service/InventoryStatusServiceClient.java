package com.example.orderservice.service;

import com.example.orderservice.config.AuthenticationRequestInterceptor;
import com.example.orderservice.dto.request.InventoryRequest;
import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.dto.response.InventoryStatusResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory-status-service", url = "http://localhost:8888/api/v1/inventory_status",
        configuration = { AuthenticationRequestInterceptor.class })
public interface InventoryStatusServiceClient {
    @GetMapping("/name")
    ApiResponse<InventoryStatusResponse> getInventoryStatusByName(@RequestParam String name);
}
