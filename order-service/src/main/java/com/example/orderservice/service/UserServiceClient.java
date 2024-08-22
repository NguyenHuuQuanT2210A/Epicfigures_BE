package com.example.orderservice.service;

import com.example.common.dto.ProductDTO;
import com.example.common.dto.UserDTO;
import com.example.orderservice.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "user-service", url = "http://localhost:8081/api/v1/users")
public interface UserServiceClient {

    @GetMapping("/{id}")
    ApiResponse<UserDTO> getUserById(@PathVariable("id") Long id);
}
