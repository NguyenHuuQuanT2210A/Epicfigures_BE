package com.example.productservice.mapper;

import com.example.productservice.dto.request.ProductImageRequest;
import com.example.productservice.dto.response.ProductImageResponse;
import com.example.productservice.entities.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {
    ProductImageMapper INSTANCE = Mappers.getMapper(ProductImageMapper.class);
    ProductImageResponse toProductImageResponse(ProductImage productImage);
    ProductImage toProductImage(ProductImageRequest request);
}