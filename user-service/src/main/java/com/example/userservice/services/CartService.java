package com.example.userservice.services;

import com.example.userservice.dtos.response.CartResponse;
import com.example.userservice.entities.UserAndProductId;
import com.example.userservice.entities.Cart;

import java.util.List;

public interface CartService {
    List<Cart> getAllCart();
    Cart getCartById(UserAndProductId ids);
    List<CartResponse> getCartByUserId(Long userId);
    List<CartResponse> getCartByProductId(Long productId);
    Cart addCart(Cart cart);
    void deleteCart(UserAndProductId id);
    void deleteCarts(List<UserAndProductId> ids);
    void deleteCartByUserId(Long userId);
    Cart updateQuantity(UserAndProductId ids, Integer quantity);
}
