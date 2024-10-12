package com.example.productservice.services;

import com.example.productservice.dto.request.CategoryRequest;
import com.example.productservice.dto.request.ProductRequest;
import com.example.productservice.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public interface ProductService {
    Long countProducts();
    Page<ProductResponse> getAllProducts(Pageable pageable);
    ProductResponse getProductByName(String name);
    Page<ProductResponse> getProductByNameLike(String name, Pageable pageable);
    ProductResponse getProductById(Long id);
    Page<ProductResponse> findByCategory(Pageable pageable, Long categoryId);
    void addProduct(ProductRequest request, List<MultipartFile> imageFiles) throws IOException;
    void updateProduct(long id, ProductRequest request, List<MultipartFile> imageFiles) throws IOException;
    void updateStockQuantity(long id, Integer stockQuantity);
    void deleteProduct(long id);
    void moveToTrash(Long id);
    Page<ProductResponse> getInTrash(Pageable pageable);
    List<ProductResponse> getProductsByIds(Set<Long> productIds);
    void restoreProduct(Long id);
    Page<ProductResponse> searchBySpecification(Pageable pageable, String sort, String[] product, String category);

    ProductResponse getProductByCode(String code);

    void updateSoldQuantity(Long id, Integer quantity);
}
