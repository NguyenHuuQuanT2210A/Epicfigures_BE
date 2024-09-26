package com.example.orderservice.service.impl;

import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.dto.response.UserResponse;
import com.example.orderservice.service.UserServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceClientImpl {
    private final UserServiceClient userServiceClient;

    public ApiResponse<UserResponse> getUserById(Long id) {
        return userServiceClient.getUserById(id);
    }
}
