package com.example.productservice.repositories;

import com.example.productservice.entities.Category;
import com.example.productservice.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Product findByNameAndDeletedAtIsNull(String name);
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name% AND p.deletedAt IS NULL")
    Page<Product> findByNameLikeAndDeletedAtIsNull(String name, Pageable pageable);
    Page<Product> findByCategoryAndDeletedAtIsNull(Pageable pageable,Category category);
    Page<Product> findAll(Pageable pageable);
    Page<Product> findByDeletedAtIsNotNull(Pageable pageable);
    Page<Product> findByDeletedAtIsNull(Pageable pageable);
    boolean existsByNameAndDeletedAtIsNull(String name);
    List<Product> findByProductIdInAndDeletedAtIsNull(Set<Long> productIds);
    boolean existsByCodeProductAndDeletedAtIsNull(String codeProduct);
}