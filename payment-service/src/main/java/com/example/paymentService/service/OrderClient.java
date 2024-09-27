package com.example.paymentService.service;

import com.example.paymentService.config.AuthenticationRequestInterceptor;
import com.example.paymentService.dto.response.ApiResponse;
import com.example.paymentService.dto.response.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-service", url = "http://localhost:8084/api/v1/orders",
        configuration = { AuthenticationRequestInterceptor.class })
public interface OrderClient {
    @GetMapping("/{id}")
    ApiResponse<OrderResponse> getOrderById(@PathVariable String id);

    @PutMapping("/changePaymentMethod/{id}")
    ApiResponse<OrderResponse> changePaymentMethod(@PathVariable String id, @RequestParam String paymentMethod);
}
