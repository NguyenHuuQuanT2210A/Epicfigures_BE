package com.example.productservice.repositories;

import com.example.productservice.entities.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductProductId(Long productId);

    @Modifying
    @Query("DELETE FROM ProductImage p WHERE p.imageId = :imageId")
    void deleteById(@Param("imageId") Long imageId);

    @Modifying
    @Query("DELETE FROM ProductImage p WHERE p.product.productId = :productId")
    void deleteAllImagesByProductId(@Param("productId") Long productId);

    @Modifying
    @Query("DELETE FROM ProductImage p WHERE p.product.productId IN :productIds")
    void deleteAllImagesByProductIds(@Param("productIds") List<Long> productIds);
}
