package com.example.orderservice.service;

import com.example.common.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceClientImpl {
    private final UserServiceClient userServiceClient;

    public UserDTO getUserById(Long id) {
        return userServiceClient.getUserById(id);
    }
}
