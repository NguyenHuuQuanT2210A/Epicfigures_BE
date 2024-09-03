package com.example.productservice.mapper;

import com.example.productservice.dto.request.CartItemRequest;
import com.example.productservice.dto.response.CartItemResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartItemResponse toCartItemResponse(CartItemRequest request);
}
