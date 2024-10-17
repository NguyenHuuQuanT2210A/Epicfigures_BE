package com.example.productservice.mapper;

import com.example.productservice.dto.request.ProductQuantityRequest;
import com.example.productservice.dto.response.ProductQuantityResponse;
import com.example.productservice.entities.ProductQuantity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductQuantityMapper {
    ProductQuantityMapper INSTANCE = Mappers.getMapper(ProductQuantityMapper.class);
    @Mapping(target = "productId", source = "product.productId")
    ProductQuantityResponse toProductQuantityResponse(ProductQuantity productQuantity);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductQuantity toProductQuantity(ProductQuantityRequest request);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatedProductQuantity(@MappingTarget ProductQuantity productQuantity, ProductQuantityRequest request);
}