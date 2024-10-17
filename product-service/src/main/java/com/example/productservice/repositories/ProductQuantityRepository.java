package com.example.productservice.repositories;

import com.example.productservice.entities.Product;
import com.example.productservice.entities.ProductQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductQuantityRepository extends JpaRepository<ProductQuantity, Long> {
    ProductQuantity findByProduct(Product product);
    boolean existsByProduct(Product product);
}