package com.example.notificationService.service;

import com.example.notificationService.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-notify-service", url = "http://localhost:8081/api/v1/users")
public interface UserClient {
    @GetMapping(path = "/{id}")
    ApiResponse<?> getUserById(@PathVariable(name = "id") Long id);
}
