//package com.example.productservice.services;
//
//import com.example.productservice.entities.FavoriteProducts;
//import com.example.productservice.entities.Product;
//import com.example.productservice.entities.User;
//import com.example.productservice.exception.EntityNotFoundException;
//import com.example.productservice.repositories.FavoriteProductRepository;
//import com.example.productservice.repositories.ProductRepository;
//import com.example.productservice.repositories.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//
//@Service
//public class FavoriteProductServiceImpl implements FavoriteProductService {
//
//    @Autowired
//    private FavoriteProductRepository favoriteProductRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Override
//    public Page<Product> getAllFavoriteProducts(Long userId, Pageable pageable) {
//        Page<FavoriteProducts> favoriteProductsPage = favoriteProductRepository.findByUserId(userId, pageable);
//        return favoriteProductsPage.map(FavoriteProducts::getProduct);
//    }
//
//
//    @Transactional
//    public void addFavoriteProduct(Long userId, Long productId) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
//        Product product = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("Product not found"));
//
//        FavoriteProducts favoriteProducts = new FavoriteProducts();
//        favoriteProducts.setUser(user);
//        favoriteProducts.setProduct(product);
//        favoriteProducts.setCreateAt(LocalDateTime.now());
//
//        favoriteProductRepository.save(favoriteProducts);
//    }
//
//    @Transactional
//    public void deleteFavoriteProduct(Long userId, Long productId) {
//        favoriteProductRepository.deleteByUserIdAndProductId(userId, productId);
//    }
//}
