package com.example.productservice.mapper;

import com.example.productservice.dto.ProductImageDTO;
import com.example.productservice.entities.ProductImage;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {
    ProductImageMapper INSTANCE = Mappers.getMapper(ProductImageMapper.class);
    ProductImageDTO productImageToProductImageDTO(ProductImage productImage);
    ProductImage productImageDTOToProductImage(ProductImageDTO productImageDTO);
}