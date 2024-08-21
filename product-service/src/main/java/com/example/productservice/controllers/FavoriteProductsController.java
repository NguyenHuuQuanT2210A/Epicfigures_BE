//package com.example.productservice.controllers;
//
//import com.example.productservice.entities.FavoriteProducts;
//import com.example.productservice.entities.Product;
//import com.example.productservice.service.FavoriteProductService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@CrossOrigin
//@RequestMapping("/api/v1/favorite-products")
//public class FavoriteProductsController {
//
//    @Autowired
//    FavoriteProductService favoriteProductService;
//
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<Page<Product>> getAllFavoriteProduct(
//            @PathVariable Long userId,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "5") int size) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Product> favoriteProducts = favoriteProductService.getAllFavoriteProducts(userId, pageable);
//        return ResponseEntity.ok(favoriteProducts);
//    }
//
//    @PostMapping
//    public ResponseEntity<Void> addFavoriteProduct(@RequestParam Long userId, @RequestParam Long productId) {
//        favoriteProductService.addFavoriteProduct(userId, productId);
//        return ResponseEntity.status(201).build();
//    }
//
//    @DeleteMapping
//    public ResponseEntity<Void> deleteFavoriteProduct(@RequestParam Long userId, @RequestParam Long productId) {
//        favoriteProductService.deleteFavoriteProduct(userId, productId);
//        return ResponseEntity.noContent().build();
//    }
//}
