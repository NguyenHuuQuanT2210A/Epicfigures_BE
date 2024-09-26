package com.example.orderservice.service;

import com.example.orderservice.config.AuthenticationRequestInterceptor;
import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service-get", url = "http://localhost:8081/api/v1/users",
        configuration = { AuthenticationRequestInterceptor.class })
public interface UserServiceClient {

    @GetMapping("/{id}")
    ApiResponse<UserResponse> getUserById(@PathVariable("id") Long id);
}
