package com.example.orderservice.service;

import com.example.common.dto.UserDTO;
import com.example.orderservice.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceClientImpl {
    private final UserServiceClient userServiceClient;

    public ApiResponse<UserDTO> getUserById(Long id) {
        return userServiceClient.getUserById(id);
    }
}
