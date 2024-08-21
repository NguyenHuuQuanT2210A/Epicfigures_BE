package com.example.productservice.services;

import com.example.productservice.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoriteProductService {
    Page<Product> getAllFavoriteProducts(Long userId, Pageable pageable);
    void addFavoriteProduct(Long userId, Long productId);
    void deleteFavoriteProduct(Long userId, Long productId);
}
