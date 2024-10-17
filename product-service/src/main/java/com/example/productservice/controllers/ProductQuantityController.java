package com.example.productservice.controllers;

import com.example.productservice.dto.request.ProductQuantityRequest;
import com.example.productservice.dto.response.ApiResponse;
import com.example.productservice.dto.response.ProductQuantityResponse;
import com.example.productservice.services.ProductQuantityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product_quantity")
public class ProductQuantityController {
    private final ProductQuantityService productQuantityService;

    @GetMapping("/getAll")
    ApiResponse<Page<ProductQuantityResponse>> getAllProductQuantity(@RequestParam(defaultValue = "1", name = "page") int page, @RequestParam(defaultValue = "10", name = "limit") int limit) {
        Page<ProductQuantityResponse> productQuantityDTOS = productQuantityService.getAllCategories(PageRequest.of(page - 1, limit, Sort.by("createdAt").descending()));
        return ApiResponse.<Page<ProductQuantityResponse>>builder()
                .message("Get all ProductQuantity")
                .data(productQuantityDTOS)
                .build();
    }

    @GetMapping("/id/{id}")
    ApiResponse<ProductQuantityResponse> getProductQuantityById(@PathVariable Long id) {
        return ApiResponse.<ProductQuantityResponse>builder()
                .message("Get ProductQuantity By Id")
                .data(productQuantityService.getProductQuantityById(id))
                .build();
    }

    @GetMapping("/productId/{productId}")
    ApiResponse<ProductQuantityResponse> getProductQuantityByProductId(@PathVariable Long productId) {
        return ApiResponse.<ProductQuantityResponse>builder()
                .message("Get ProductQuantity By Product Id")
                .data(productQuantityService.getProductQuantityByProductId(productId))
                .build();
    }

    @PostMapping
    ResponseEntity<?> addProductQuantity(@Valid @RequestBody ProductQuantityRequest request) {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(HttpStatus.CREATED.value())
                .message("Created ProductQuantity Successfully")
                .data(productQuantityService.addProductQuantity(request))
                .build());
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateProductQuantity(@PathVariable Long id, @Valid @RequestBody ProductQuantityRequest request) {
        productQuantityService.updateProductQuantity(id, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Update ProductQuantity Successfully!")
                .build());
    }

    @DeleteMapping("/{id}")
    ApiResponse<?> deleteProductQuantity(@PathVariable Long id) {
        productQuantityService.deleteProductQuantity(id);
        return ApiResponse.builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Delete ProductQuantity Successfully")
                .build();
    }
}
