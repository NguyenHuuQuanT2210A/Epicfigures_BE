package com.example.productservice.services;

import com.example.productservice.dto.response.ProductImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductImageService {
    void deleteProductImage(Long id) throws IOException;
    void deleteProductImages(Long productId) throws IOException;
    void deleteProductImages(List<Long> productIds) throws IOException;
    List<ProductImageResponse> getProductImages(Long productId);
    List<ProductImageResponse> getProductImagesByProductId(Long productId);
    List<ProductImageResponse> saveProductImage(Long productId, List<MultipartFile> imageFile) throws IOException;
    List<ProductImageResponse> updateProductImage(Long productId, List<Long> productImageIds, List<MultipartFile> imageFile) throws IOException;
    List<ProductImageResponse> isProductImagesExist(List<Long> productImageIds);
}
