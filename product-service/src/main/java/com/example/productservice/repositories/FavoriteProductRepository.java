//package com.example.productservice.repositories;
//
//import com.example.productservice.entities.FavoriteProducts;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface FavoriteProductRepository extends JpaRepository<FavoriteProducts, Long> {
//    Page<FavoriteProducts> findByUserId(Long userId, Pageable pageable);
//    void deleteByUserIdAndProductId(Long userId, Long productId);
//}
