package com.example.productservice.repositories;

import com.example.productservice.entities.Category;
import com.example.productservice.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Product findByNameAndDeletedAtIsNull(String name);
    Page<Product> findByCategoryAndDeletedAtIsNull(Pageable pageable,Category category);
    Page<Product> findAll(Pageable pageable);
    Page<Product> findByDeletedAtIsNotNull(Pageable pageable);
    Page<Product> findByDeletedAtIsNull(Pageable pageable);
    boolean existsByName(String name);
    List<Product> findByProductIdIn(Set<Long> productIds);
}