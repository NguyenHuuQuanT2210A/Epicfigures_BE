package com.example.userservice.services.impl;

import com.example.userservice.dtos.response.CartResponse;
import com.example.userservice.dtos.response.ProductImageResponse;
import com.example.userservice.entities.UserAndProductId;
import com.example.userservice.entities.Cart;
import com.example.userservice.exceptions.AppException;
import com.example.userservice.exceptions.ErrorCode;
import com.example.userservice.repositories.CartRepository;
import com.example.userservice.services.CartService;
import com.example.userservice.services.ProductClient;
import com.example.userservice.statics.enums.CartStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
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
    public List<CartResponse> getCartByUserId(Long userId) {
        var carts = cartRepository.findAllByUserId(userId);
        List<Cart> cartsToCheck = new ArrayList<>();
        for (var cart : carts) {
            var product = productClient.getProductById(cart.getId().getProductId());
            if (product.getData() == null) {
                cart.setStatus(CartStatus.DISCONTINUED);
            }else if (product.getData().getStockQuantity() < cart.getQuantity()) {
                cart.setStatus(CartStatus.EXCEEDED_AVAILABLE_STOCK);
            }else if (product.getData().getStockQuantity() == 0) {
                cart.setStatus(CartStatus.OUT_OF_STOCK);
            }else if (product.getData().getPrice().compareTo(cart.getUnitPrice().divide(BigDecimal.valueOf(cart.getQuantity()))) != 0) {
                cart.setStatus(CartStatus.PRICE_CHANGED);
            }
            cartRepository.save(cart);
            cartsToCheck.add(cart);
        }
        return getCartResponse(cartsToCheck);
    }

    @Override
    public List<CartResponse> getCartByProductId(Long productId) {
        var carts = cartRepository.findAllByProductId(productId);
        return getCartResponse(carts);
    }

    @Override
    public Cart addCart(Cart cart) {
        var cartExist = getCartById(cart.getId());
        var product = productClient.getProductById(cart.getId().getProductId());

        if (cartExist == null) {
            return cartRepository.save(Cart.builder().id(cart.getId())
                    .quantity(cart.getQuantity())
                    .unitPrice(BigDecimal.valueOf(cart.getQuantity()).multiply(product.getData().getPrice()))
                    .status(CartStatus.AVAILABLE)
                    .build());

        }else {
            if (product.getData().getStockQuantity() < cart.getQuantity() + cartExist.getQuantity())
            {
                throw new AppException(ErrorCode.EXCEED_PRODUCT_QUANTITY);
            }
            cartExist.setQuantity(cart.getQuantity() + cartExist.getQuantity());
            cartExist.setUnitPrice(BigDecimal.valueOf(cart.getQuantity()).multiply(product.getData().getPrice()).add(cartExist.getUnitPrice()));
            return cartRepository.save(cartExist);
        }
    }

    @Override
    public Cart updateQuantity(UserAndProductId ids, Integer quantity) {
        Cart cart = getCartById(ids);
        var product = productClient.getProductById(cart.getId().getProductId());
        if (product.getData().getStockQuantity() < quantity)
        {
            throw new AppException(ErrorCode.EXCEED_PRODUCT_QUANTITY);
        }
        cart.setQuantity(quantity);
        cart.setUnitPrice(BigDecimal.valueOf(quantity).multiply(product.getData().getPrice()));
        return cartRepository.save(cart);
    }

    @Override
    public void deleteCart(UserAndProductId id) {
        cartRepository.deleteById(id);
    }

    @Override
    public void deleteCarts(List<UserAndProductId> ids) {
        for (UserAndProductId id : ids) {
            deleteCart(id);
        }
    }


    @Override
    @Transactional
    public void deleteCartByUserId(Long userId) {
        cartRepository.deleteCartsByUserId(userId);
    }

    private List<CartResponse> getCartResponse(List<Cart> carts){
        List<CartResponse> cartResponses = new ArrayList<>();

        for (var cart : carts) {
            Set<String> productImagesUrl = new HashSet<>();
            var product = productClient.getProductById(cart.getId().getProductId());
            for (ProductImageResponse productImage : product.getData().getImages()) {
                productImagesUrl.add(productImage.getImageUrl());
            }
            cartResponses.add(CartResponse.builder()
                    .id(cart.getId())
                    .quantity(cart.getQuantity())
                    .productName(product.getData().getName())
                    .productPrice(product.getData().getPrice())
                    .description(product.getData().getDescription())
                    .status(cart.getStatus())
                    .unitPrice(cart.getUnitPrice())
                    .productImages(productImagesUrl)
                    .build());
        }
        return cartResponses;
    }

}
