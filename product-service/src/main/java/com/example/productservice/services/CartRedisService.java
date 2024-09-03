package com.example.productservice.services;

import com.example.productservice.dto.UserAndProductId;
import com.example.productservice.dto.request.CartItemRequest;
import com.example.productservice.dto.response.CartItemResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface CartRedisService {
    CartItemResponse getCartById(UserAndProductId id);
    List<CartItemResponse> getCartByUserId(Long userId);
    void addCart(CartItemRequest request);
    void deleteCart(UserAndProductId id);
    void deleteCarts(List<UserAndProductId> ids);
    void deleteCartByUserId(Long userId);
    void updateQuantity(UserAndProductId ids, Integer quantity);
}
