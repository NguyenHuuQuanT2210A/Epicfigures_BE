package com.example.paymentService.service;

import com.example.paymentService.dto.response.ApiResponse;
import com.example.paymentService.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service-order", url = "http://localhost:8081/api/v1/users")
public interface UserClient {
    @GetMapping(path = "/{id}")
    ApiResponse<UserResponse> getUserById(@PathVariable(name = "id") Long id);
}
