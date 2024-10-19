package com.example.orderservice.service.impl;

import com.example.orderservice.dto.request.ProductQuantityRequest;
import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.dto.response.ProductResponse;
import com.example.orderservice.service.ProductServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductServiceClientImpl {
    private final ProductServiceClient productServiceClient;

    public ApiResponse<ProductResponse> getProductById(Long id) {
        return productServiceClient.getProductById(id);
    }

    public ApiResponse<List<ProductResponse>> getProductsByIds(Set<Long> productIds) {
        return productServiceClient.getProductsByIds(productIds);
    }

//    public void updateSoldQuantity(Long id, Integer quantity){
//        productServiceClient.updateSoldQuantity(id, quantity);
//    }

    public void updateQuantity(Long id, ProductQuantityRequest request){
        productServiceClient.updateQuantity(id, request);
    }
}
