package com.example.productservice.controllers;

import com.example.productservice.dto.request.CartItemRequest;
import com.example.productservice.dto.response.CartItemResponse;
import com.example.productservice.dto.UserAndProductId;
import com.example.productservice.dto.response.ApiResponse;
import com.example.productservice.services.CartRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/cart")
public class CartController {
    private final CartRedisService cartRedisService;

//    @GetMapping
//    ApiResponse<List<CartItemResponse>> getAll() {
//        return ApiResponse.<List<CartItemResponse>>builder()
//                .message("Get all cart")
//                .data(cartService.getAllCart())
//                .build();
//    }

    @GetMapping("/cartId")
    ApiResponse<CartItemResponse> getById(@RequestBody UserAndProductId id) {
        return ApiResponse.<CartItemResponse>builder()
                .message("Get cart by Id")
                .data(cartRedisService.getCartById(id))
                .build();
    }

    @GetMapping("/user/{userId}")
    ApiResponse<List<CartItemResponse>> getByUserId(@PathVariable Long userId) {
        return ApiResponse.<List<CartItemResponse>>builder()
                .message("get cart by userId")
                .data(cartRedisService.getCartByUserId(userId))
                .build();
    }

//    @GetMapping("/product/{productId}")
//    ApiResponse<List<CartResponse>> getByProductId(@PathVariable Long productId) {
//        return ApiResponse.<List<CartResponse>>builder()
//                .message("get cart by productId")
//                .data(cartService.getCartByProductId(productId))
//                .build();
//    }

    @PostMapping
    ApiResponse<CartItemResponse> createCart(@RequestBody CartItemRequest request) {
        cartRedisService.addCart(request);
        return ApiResponse.<CartItemResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Create cart successfully")
                .build();
    }

    @PutMapping("/updateQuantity")
    ResponseEntity<ApiResponse<Void>> updateQuantity(@RequestBody UserAndProductId ids, @RequestParam int quantity) {
        cartRedisService.updateQuantity(ids, quantity);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Updated Quantity Cart Successfully")
                .build());
    }

    @DeleteMapping
    ResponseEntity<ApiResponse<String>> deleteById(@RequestBody UserAndProductId id) {
        cartRedisService.deleteCart(id);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .message("Deleted Cart Successfully")
                .build());
    }

    @DeleteMapping("/ids")
    ResponseEntity<ApiResponse<String>> deleteByIds(@RequestBody List<UserAndProductId> ids) {
        cartRedisService.deleteCarts(ids);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .message("Deleted Carts Successfully")
                .build());
    }

    @DeleteMapping("/user/{userId}")
    ResponseEntity<ApiResponse<String>> deleteByUserId(@PathVariable Long userId) {
        cartRedisService.deleteCartByUserId(userId);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .message("Deleted Cart by userId Successfully")
                .build());
    }
}
