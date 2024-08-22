package com.example.userservice.controllers;

import com.example.userservice.dtos.response.ApiResponse;
import com.example.userservice.dtos.response.CartResponse;
import com.example.userservice.entities.UserAndProductId;
import com.example.userservice.entities.Cart;
import com.example.userservice.services.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Cart", description = "Cart Controller")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/cart")
public class CartController {
    private final CartService cartService;

    @GetMapping
    ApiResponse<List<Cart>> getAll() {
        return ApiResponse.<List<Cart>>builder()
                .message("Get all cart")
                .data(cartService.getAllCart())
                .build();
    }

    @GetMapping("/cartId")
    ApiResponse<Cart> getById(@RequestBody UserAndProductId ids) {
        return ApiResponse.<Cart>builder()
                .message("Get cart by Id")
                .data(cartService.getCartById(ids))
                .build();
    }

    @GetMapping("/user/{userId}")
    ApiResponse<List<CartResponse>> getByUserId(@PathVariable Long userId) {
        return ApiResponse.<List<CartResponse>>builder()
                .message("get cart by userId")
                .data(cartService.getCartByUserId(userId))
                .build();
    }

    @GetMapping("/product/{productId}")
    ApiResponse<List<CartResponse>> getByProductId(@PathVariable Long productId) {
        return ApiResponse.<List<CartResponse>>builder()
                .message("get cart by productId")
                .data(cartService.getCartByProductId(productId))
                .build();
    }

    @PostMapping
    ApiResponse<Cart> createCart(@RequestBody Cart cart) {
        return ApiResponse.<Cart>builder()
                .code(HttpStatus.CREATED.value())
                .message("Create cart")
                .data(cartService.addCart(cart))
                .build();
    }

    @PutMapping("/updateQuantity")
    ResponseEntity<ApiResponse<Cart>> updateQuantity(@RequestBody UserAndProductId ids, @RequestParam int quantity) {
        return ResponseEntity.ok(ApiResponse.<Cart>builder()
                .message("Updated Quantity Cart")
                .data(cartService.updateQuantity(ids, quantity))
                .build());
    }

    @DeleteMapping
    ResponseEntity<ApiResponse<String>> deleteById(@RequestBody UserAndProductId ids) {
        cartService.deleteCart(ids);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .message("Deleted Cart Successfully")
                .build());
    }

    @DeleteMapping("/user/{userId}")
    ResponseEntity<ApiResponse<String>> deleteByUserId(@PathVariable Long userId) {
        cartService.deleteCartByUserId(userId);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .message("Deleted Cart by userId Successfully")
                .build());
    }
}
