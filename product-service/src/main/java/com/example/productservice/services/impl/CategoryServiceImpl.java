package com.example.productservice.services.impl;

import com.example.productservice.dto.request.CategoryRequest;
import com.example.productservice.dto.response.CategoryResponse;
import com.example.productservice.entities.Category;
import com.example.productservice.exception.CategoryNotFoundException;
import com.example.productservice.exception.CustomException;
import com.example.productservice.mapper.CategoryMapper;
import com.example.productservice.repositories.CategoryRepository;
import com.example.productservice.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Page<CategoryResponse> getAllCategories(Pageable pageable) {
        Page<Category> categories = categoryRepository.findByDeletedAtIsNull(pageable);
        return categories.map(CategoryMapper.INSTANCE::toCategoryResponse);
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = findCategoryById(id);
        return CategoryMapper.INSTANCE.toCategoryResponse(category);
    }

    @Override
    public Page<CategoryResponse> getCategoryByParentCategoryId(Long parentCategoryId, Pageable pageable) {
        return categoryRepository.findByParentCategoryIdAndDeletedAtIsNull(parentCategoryId, pageable).map(CategoryMapper.INSTANCE::toCategoryResponse);
    }

    @Override
    public Page<CategoryResponse> getCategoriesByParentCategoryIsNull(Pageable pageable) {
        return categoryRepository.findByParentCategoryIsNullAndDeletedAtIsNull(pageable).map(CategoryMapper.INSTANCE::toCategoryResponse);
    }

    @Override
    public CategoryResponse addCategory(CategoryRequest request) {

        if(categoryRepository.existsByCategoryNameAndDeletedAtIsNull(request.getCategoryName())){
            throw new CustomException("Category already exists with name: " + request.getCategoryName(), HttpStatus.BAD_REQUEST);
        }

        Category category = CategoryMapper.INSTANCE.toCategory(request);
        if (request.getParentCategoryId() != 0){
            category.setParentCategory(findCategoryById(request.getParentCategoryId()));
        }
        categoryRepository.save(category);
        return CategoryMapper.INSTANCE.toCategoryResponse(category);
    }

    @Override
    public void updateCategory(Long id, CategoryRequest request) {
        Category category = findCategoryById(id);

        if (category.getDeletedAt() != null){
            throw new CustomException("Category is in trash with id: " + id, HttpStatus.BAD_REQUEST);
        }

        if (categoryRepository.existsByCategoryNameAndDeletedAtIsNull(request.getCategoryName()) && !request.getCategoryName().equals(category.getCategoryName())) {
            throw new CustomException("Category already exists with name: " + request.getCategoryName(), HttpStatus.BAD_REQUEST);
        }

        if (request.getParentCategoryId() != 0 && !request.getParentCategoryId().equals(category.getCategoryId())) {
            category.setParentCategory(findCategoryById(request.getParentCategoryId()));
        }

        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());

        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        findCategoryById(id);
        categoryRepository.deleteById(id);
    }

    @Override
    public Page<CategoryResponse> getCategoryByName(String name, Pageable pageable) {
        return categoryRepository.findCategoriesByCategoryNameLikeAndDeletedAtIsNull(name, pageable).map(CategoryMapper.INSTANCE::toCategoryResponse);
    }

    public void moveToTrash(Long id) {
        Category category = findCategoryById(id);

        LocalDateTime now = LocalDateTime.now();
        category.setDeletedAt(now);
        categoryRepository.save(category);
    }

    @Override
    public Page<CategoryResponse> getInTrash(Pageable pageable) {
        Page<Category> categories = categoryRepository.findByDeletedAtIsNotNull(pageable);
        return categories.map(CategoryMapper.INSTANCE::toCategoryResponse);
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
    }

    @Override
    public void restoreCategory(Long id) {
        Category category = findCategoryById(id);
        if (category == null) {
            throw new CategoryNotFoundException("Cannot find this category id: " + id);
        }
        category.setDeletedAt(null);
        categoryRepository.save(category);
    }

    @Override
    public Long countCategories() {
        return categoryRepository.count();
    }

}