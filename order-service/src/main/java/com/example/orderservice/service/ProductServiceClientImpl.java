package com.example.orderservice.service;

import com.example.common.dto.ProductDTO;
import com.example.orderservice.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductServiceClientImpl {
    private final ProductServiceClient productServiceClient;

    public ApiResponse<ProductDTO> getProductById(Long id) {
        return productServiceClient.getProductById(id);
    }

    public ApiResponse<List<ProductDTO>> getProductsByIds(Set<Long> productIds) {
        return productServiceClient.getProductsByIds(productIds);
    }
}
