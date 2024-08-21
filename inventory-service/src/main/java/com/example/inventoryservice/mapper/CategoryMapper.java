//package com.example.inventoryservice.mapper;
//
//import com.example.productservice.dto.CategoryDTO;
//import com.example.productservice.entities.Category;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.factory.Mappers;
//
//@Mapper(componentModel = "spring")
//public interface CategoryMapper {
//    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
//    @Mapping(target = "products", ignore = true)
//    CategoryDTO categoryToCategoryDTO(Category category);
//    Category categoryDTOToCategory(CategoryDTO categoryDTO);
//}
