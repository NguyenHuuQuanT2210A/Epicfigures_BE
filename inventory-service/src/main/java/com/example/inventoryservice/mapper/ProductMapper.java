//package com.example.inventoryservice.mapper;
//
//import com.example.productservice.dto.ProductDTO;
//import com.example.productservice.entities.Product;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.factory.Mappers;
//
//@Mapper(componentModel = "spring")
//public interface ProductMapper {
//    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
//    @Mapping(target = "images", ignore = true)
//    ProductDTO productToProductDTO(Product product);
//    Product productDTOToProduct(ProductDTO productDTO);
//}