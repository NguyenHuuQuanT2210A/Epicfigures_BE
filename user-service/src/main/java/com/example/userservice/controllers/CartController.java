package com.example.userservice.controllers;

import com.example.userservice.dtos.response.ApiResponse;
import com.example.userservice.entities.UserAndProductId;
import com.example.userservice.entities.Cart;
import com.example.userservice.services.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Cart", description = "Cart Controller")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(path = "api/v1/cart")
public class CartController {
    CartService cartService;

    @GetMapping
    ApiResponse<List<Cart>> getAll() {
        return ApiResponse.<List<Cart>>builder()
                .message("Get all cart data")
                .result(cartService.getAllCart())
                .build();
    }

    @GetMapping("/cartId")
    ApiResponse<Cart> getById(@RequestBody UserAndProductId ids) {
        return ApiResponse.<Cart>builder()
                .message("Get cart by Id")
                .result(cartService.getCartById(ids))
                .build();
    }

    @GetMapping("/user/{userId}")
    ApiResponse<List<Cart>> getByUserId(@PathVariable Long userId) {
        return ApiResponse.<List<Cart>>builder()
                .message("get cart by userId")
                .result(cartService.getCartByUserId(userId))
                .build();
    }

    @GetMapping("/product/{productId}")
    ApiResponse<List<Cart>> getByProductId(@PathVariable Long productId) {
        return ApiResponse.<List<Cart>>builder()
                .message("get cart by productId")
                .result(cartService.getCartByProductId(productId))
                .build();
    }

    @PostMapping
    ApiResponse<Cart> createCart(@RequestBody Cart cart) {
        return ApiResponse.<Cart>builder()
                .code(201)
                .message("Created!")
                .result(cartService.addCart(cart))
                .build();
    }

    @PutMapping("/updateQuantity")
    ApiResponse<Cart> updateQuantity(@RequestBody UserAndProductId ids, @RequestParam int quantity) {
        return ApiResponse.<Cart>builder()
                .message("Updated Quantity Cart")
                .result(cartService.updateQuantity(ids, quantity))
                .build();
    }

    @DeleteMapping
    ApiResponse<String> deleteById(@RequestBody UserAndProductId ids) {
        cartService.deleteCart(ids);
        return ApiResponse.<String>builder()
                .message("Deleted Cart by Id")
                .build();
    }

    @DeleteMapping("/user/{userId}")
    ApiResponse<String> deleteByUserId(@PathVariable Long userId) {
        cartService.deleteCartByUserId(userId);
        return ApiResponse.<String>builder()
                .message("Deleted Cart by userId")
                .build();
    }
}
