package com.example.productservice.services;

import com.example.productservice.dto.response.ProductImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageService {
    void deleteProductImage(Long id);
    void deleteProductImages(Long productId);
    void deleteProductImages(List<Long> productIds);
    List<ProductImageResponse> getProductImages(Long productId);
    List<ProductImageResponse> getProductImagesByProductId(Long productId);
    List<ProductImageResponse> saveProductImage(Long productId, List<MultipartFile> imageFile);
    List<ProductImageResponse> updateProductImage(Long productId, List<Long> productImageIds, List<MultipartFile> imageFile);
    List<ProductImageResponse> isProductImagesExist(List<Long> productImageIds);
}
