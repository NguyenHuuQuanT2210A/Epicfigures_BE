package com.example.orderservice.service;

import com.example.common.dto.UserDTO;
import com.example.orderservice.config.AuthenticationRequestInterceptor;
import com.example.orderservice.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service-get", url = "http://localhost:8081/api/v1/users",
        configuration = { AuthenticationRequestInterceptor.class })
public interface UserServiceClient {

    @GetMapping("/{id}")
    ApiResponse<UserDTO> getUserById(@PathVariable("id") Long id);
}
