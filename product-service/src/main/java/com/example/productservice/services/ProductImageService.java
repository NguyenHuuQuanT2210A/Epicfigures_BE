package com.example.productservice.services;

import com.example.productservice.dto.ProductImageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageService {
    void deleteProductImage(Long id);
    void deleteProductImages(Long productId);
    void deleteProductImages(List<Long> productIds);
    List<ProductImageDTO> getProductImages(Long productId);
    List<ProductImageDTO> saveProductImage(Long productId, List<MultipartFile> imageFile);
    List<ProductImageDTO> updateProductImage(Long productId, List<Long> productImageIds, List<MultipartFile> imageFile);
}
