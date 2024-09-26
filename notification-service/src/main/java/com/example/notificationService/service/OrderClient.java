package com.example.notificationService.service;

import com.example.notificationService.enums.OrderSimpleStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-notify-service", url = "http://localhost:8084/api/v1/orders")
public interface OrderClient {
    @PutMapping("/changeStatus/{id}")
    String changeStatus(@PathVariable String id, @RequestParam OrderSimpleStatus status);
}