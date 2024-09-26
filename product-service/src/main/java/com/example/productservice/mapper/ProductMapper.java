package com.example.productservice.mapper;

import com.example.productservice.dto.request.ProductRequest;
import com.example.productservice.dto.response.ProductResponse;
import com.example.productservice.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
    ProductResponse toProductResponse(Product product);
    Product toProduct(ProductRequest request);
    Product productResponsetoProduct(ProductResponse response);
    List<ProductResponse> productListToProductDTOList(List<Product> products);
    List<Product> productDTOListToProductList(List<ProductResponse> productDTOs);
}