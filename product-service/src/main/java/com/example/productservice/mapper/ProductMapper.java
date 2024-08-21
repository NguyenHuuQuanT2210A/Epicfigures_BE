package com.example.productservice.mapper;

import com.example.productservice.dto.ProductDTO;
import com.example.productservice.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
    ProductDTO productToProductDTO(Product product);
    Product productDTOToProduct(ProductDTO productDTO);
    List<ProductDTO> productListToProductDTOList(List<Product> products);
    List<Product> productDTOListToProductList(List<ProductDTO> productDTOs);
}