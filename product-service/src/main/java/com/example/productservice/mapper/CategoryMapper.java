package com.example.productservice.mapper;

import com.example.productservice.dto.request.CategoryRequest;
import com.example.productservice.dto.response.CategoryResponse;
import com.example.productservice.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
    @Mapping(source = "parentCategory.categoryId", target = "parentCategoryId")
    CategoryResponse toCategoryResponse(Category category);
    Category toCategory(CategoryRequest request);
    Category categoryResponsetoCategory(CategoryResponse response);
}
