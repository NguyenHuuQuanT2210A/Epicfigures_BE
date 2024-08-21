package com.example.userservice.services.impl;

import com.example.userservice.entities.UserAndProductId;
import com.example.userservice.entities.Cart;
import com.example.userservice.repositories.CartRepository;
import com.example.userservice.services.CartService;
import com.example.userservice.services.ProductClient;
import com.example.userservice.statics.enums.CartStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService {
    CartRepository cartRepository;
    ProductClient productClient;

    @Override
    public List<Cart> getAllCart() {
        return cartRepository.findAll().stream().toList();
    }

    @Override
    public Cart getCartById(UserAndProductId ids) {
        return cartRepository.findCartById(ids);
    }

    @Override
    public List<Cart> getCartByUserId(Long userId) {
        return cartRepository.findAllByUserId(userId);
    }

    @Override
    public List<Cart> getCartByProductId(Long productId) {
        return cartRepository.findAllByProductId(productId);
    }

    @Override
    public Cart addCart(Cart cart) {
        var cartExist = getCartById(cart.getId());
        var product = productClient.getProductById(cart.getId().getProductId());

        if (cartExist == null) {
            Cart cartNew = cartRepository.save(Cart.builder().id(cart.getId())
                    .quantity(cart.getQuantity())
                    .unitPrice(BigDecimal.valueOf(cart.getQuantity()).multiply(product.getPrice()))
                    .status(CartStatus.AVAILABLE)
                    .productName(product.getName())
                    .productPrice(product.getPrice())
                    .build());
            return cartNew;
        }else {
            if (product.getStockQuantity() < cart.getQuantity() + cartExist.getQuantity())
            {
                throw new RuntimeException("Exceeding the available product quantity, please adjust the product quantity!");
            }
            cartExist.setQuantity(cart.getQuantity() + cartExist.getQuantity());
            cartExist.setUnitPrice(BigDecimal.valueOf(cart.getQuantity()).multiply(product.getPrice()).add(cartExist.getUnitPrice()));
            return cartRepository.save(cartExist);
        }
    }

    @Override
    public Cart updateQuantity(UserAndProductId ids, Integer quantity) {
        Cart cart = getCartById(ids);
        var product = productClient.getProductById(cart.getId().getProductId());
        if (product.getStockQuantity() < cart.getQuantity() + quantity)
        {
            throw new RuntimeException("Exceeding the available product quantity, please adjust the product quantity!");
        }
        cart.setQuantity(quantity);
        cart.setUnitPrice(BigDecimal.valueOf(quantity).multiply(product.getPrice()));
        return cartRepository.save(cart);
    }

    @Override
    public void deleteCart(UserAndProductId ids) {
        cartRepository.deleteById(ids);
    }

    @Override
    @Transactional
    public void deleteCartByUserId(Long userId) {
        cartRepository.deleteCartsByUserId(userId);
    }


}
