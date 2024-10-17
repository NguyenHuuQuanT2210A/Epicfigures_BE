package com.example.productservice.services;

import com.example.productservice.dto.request.ProductQuantityRequest;
import com.example.productservice.dto.response.ProductQuantityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ProductQuantityService {
    Page<ProductQuantityResponse> getAllCategories(Pageable pageable);
    ProductQuantityResponse getProductQuantityById(Long id);
    Long addProductQuantity(ProductQuantityRequest request);
    void updateProductQuantity(Long id, ProductQuantityRequest request);
    void deleteProductQuantity(Long id);

    ProductQuantityResponse getProductQuantityByProductId(Long productId);
}
