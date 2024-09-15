package com.example.productservice.services;

import com.example.productservice.dto.CategoryDTO;
import com.example.productservice.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Service
public interface ProductService {
    Long countProducts();
    Page<ProductDTO> getAllProducts(Pageable pageable);
    ProductDTO getProductByName(String name);
    ProductDTO getProductById(Long id);
    Page<ProductDTO> findByCategory(Pageable pageable, CategoryDTO category);
    void addProduct(ProductDTO productDTO, List<MultipartFile> imageFiles);
    void updateProduct(long id, ProductDTO updatedProductDTO, List<MultipartFile> imageFiles);
    void updateStockQuantity(long id, Integer stockQuantity);
    void deleteProduct(long id);
    void moveToTrash(Long id);
    Page<ProductDTO> getInTrash(Pageable pageable);
    List<ProductDTO> getProductsByIds(Set<Long> productIds);
    void restoreProduct(Long id);
    Page<ProductDTO> searchBySpecification(Pageable pageable, String sort, String[] product, String category);
}
