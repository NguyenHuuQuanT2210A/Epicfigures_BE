package com.example.productservice.services;

import com.example.productservice.dto.request.CategoryRequest;
import com.example.productservice.dto.response.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    Page<CategoryResponse> getAllCategory(Pageable pageable);
    CategoryResponse getCategoryById(Long id);
    List<CategoryResponse> getCategoryByParentCategoryId(Long parentCategoryId);
    List<CategoryResponse> getCategoriesByParentCategoryIsNull();
    CategoryResponse addCategory(CategoryRequest request);
    void updateCategory(Long id, CategoryRequest request);
    void deleteCategory(Long id);
    List<CategoryResponse> getCategoryByName(String name);
    void moveToTrash(Long id);
    Page<CategoryResponse> getInTrash(Pageable pageable);

    void restoreCategory(Long id);

    Long countCategories();
}
