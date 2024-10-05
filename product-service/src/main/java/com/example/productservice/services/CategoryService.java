package com.example.productservice.services;

import com.example.productservice.dto.request.CategoryRequest;
import com.example.productservice.dto.response.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    Page<CategoryResponse> getAllCategories(Pageable pageable);
    CategoryResponse getCategoryById(Long id);
    Page<CategoryResponse> getCategoryByParentCategoryId(Long parentCategoryId, Pageable pageable);
    Page<CategoryResponse> getCategoriesByParentCategoryIsNull(Pageable pageable);
    CategoryResponse addCategory(CategoryRequest request);
    void updateCategory(Long id, CategoryRequest request);
    void deleteCategory(Long id);
    Page<CategoryResponse> getCategoryByName(String name, Pageable pageable);
    void moveToTrash(Long id);
    Page<CategoryResponse> getInTrash(Pageable pageable);

    void restoreCategory(Long id);

    Long countCategories();
}
