package com.example.productservice.services.impl;

import com.example.productservice.dto.ProductImageDTO;
import com.example.productservice.dto.UserAndProductId;
import com.example.productservice.dto.request.CartItemRequest;
import com.example.productservice.dto.response.CartItemResponse;
import com.example.productservice.mapper.CartMapper;
import com.example.productservice.services.CartRedisService;
import com.example.productservice.services.ProductServiceImpl;
import com.example.productservice.statics.enums.CartStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.productservice.constant.CommonDefine.*;

@Service
@RequiredArgsConstructor
public class CartRedisServiceImpl implements CartRedisService {
    private final BaseRedisServiceImpl<String, String, Object> baseRedisService;

    private final CartMapper cartMapper;
    private final ProductServiceImpl productService;
    private final ObjectMapper objectMapper;

//    private CartItemResponse convertToCartItemResponse(Object object) {
//        ConvertToObject<CartItemResponse> converter = new ConvertToObject<>(new ObjectMapper());
//        return converter.convertToObjectResponse(object, CartItemResponse.class);
//    }

    private CartItemResponse convertToCartItemResponse(Object object) {
        if (object instanceof LinkedHashMap) {
            return objectMapper.convertValue(object, CartItemResponse.class);
        } else {
            return (CartItemResponse) object;
        }
    }

    @Override
    public CartItemResponse getCartById(UserAndProductId id){
        String key = CART_ITEM_USER_ID + id.getUserId().toString();
        String field = PRODUCT_ID + id.getProductId().toString();
        if (baseRedisService.hashExists(key, field)) {
            Object object = baseRedisService.hashGet(key, field);
            return convertToCartItemResponse(object);
        }
        return null;
    }

    @Override
    public List<CartItemResponse> getCartByUserId(Long userId) {
        String key = CART_ITEM_USER_ID + userId;
        if (baseRedisService.keyExists(key)) {
//            List<Object> productsMap = baseRedisService.hashGetByFieldPrefix(key, PRODUCT_ID);

            List<Object> sortedKey = baseRedisService.getKeyTimestamp(key + TIMESTAMPS);
            return sortedKey.stream()
                    .map(this::convertToCartItemResponse)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    @Override
    public void addCart(CartItemRequest request){
        String key = CART_ITEM_USER_ID + request.getId().getUserId().toString();
        String field = PRODUCT_ID + request.getId().getProductId().toString();
        var product = productService.getProductById(request.getId().getProductId());
        CartItemResponse cartItemResponse;

        if (baseRedisService.hashExists(key, field)) {
            cartItemResponse = convertToCartItemResponse(baseRedisService.hashGet(key, field));
            cartItemResponse.setQuantity(cartItemResponse.getQuantity() + request.getQuantity());
            cartItemResponse.setUnitPrice(BigDecimal.valueOf(cartItemResponse.getQuantity() + request.getQuantity()).multiply(product.getPrice()));

            baseRedisService.delete(key, field);
        }else {
            cartItemResponse = cartMapper.toCartItemResponse(request);
            cartItemResponse.setStatus(CartStatus.AVAILABLE);
            cartItemResponse.setProductName(product.getName());
            cartItemResponse.setProductPrice(product.getPrice());
            cartItemResponse.setUnitPrice(BigDecimal.valueOf(cartItemResponse.getQuantity()).multiply(product.getPrice()));
            cartItemResponse.setProductImages(product.getImages().stream().map(ProductImageDTO::getImageUrl).collect(Collectors.toSet()));
        }

        baseRedisService.hashSet(key, field, cartItemResponse);
        baseRedisService.sortedAdd(key, field);
    }

    @Override
    public void updateQuantity(UserAndProductId ids, Integer quantity) {
        String key = CART_ITEM_USER_ID + ids.getUserId().toString();
        String field = PRODUCT_ID + ids.getProductId().toString();

        if (baseRedisService.hashExists(key, field)){
            Object object = baseRedisService.hashGet(key, field);
            CartItemResponse cartItemResponse = convertToCartItemResponse(object);
            cartItemResponse.setQuantity(quantity);
            cartItemResponse.setUnitPrice(BigDecimal.valueOf(quantity).multiply(cartItemResponse.getProductPrice()));

            baseRedisService.hashSet(key, field, cartItemResponse);
        }
    }

    @Override
    public void deleteCart(UserAndProductId id) {
        baseRedisService.delete(CART_ITEM_USER_ID + id.getUserId().toString(), PRODUCT_ID + id.getProductId().toString());
    }

    @Override
    public void deleteCarts(List<UserAndProductId> ids) {
        ids.forEach(id -> baseRedisService.delete(CART_ITEM_USER_ID + id.getUserId().toString(), PRODUCT_ID + id.getProductId().toString()));
    }

    @Override
    public void deleteCartByUserId(Long userId) {
        baseRedisService.delete(CART_ITEM_USER_ID + userId);
    }
}
