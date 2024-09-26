package com.example.paymentService.service;

import com.example.paymentService.config.AuthenticationRequestInterceptor;
import com.example.paymentService.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service", url = "http://localhost:8084/api/v1/orders",
        configuration = { AuthenticationRequestInterceptor.class })
public interface OrderClient {
    @GetMapping("/{id}")
    ApiResponse<?> getOrderById(@PathVariable String id);
}
