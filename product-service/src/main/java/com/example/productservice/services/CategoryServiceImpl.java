package com.example.productservice.services;

import com.example.productservice.dto.CategoryDTO;
import com.example.productservice.entities.Category;
import com.example.productservice.exception.CategoryNotFoundException;
import com.example.productservice.exception.CustomException;
import com.example.productservice.mapper.CategoryMapper;
import com.example.productservice.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    public Page<CategoryDTO> getAllCategory(Pageable pageable) {
        Page<Category> categories = categoryRepository.findByDeletedAtIsNull(pageable);
        return categories.map(CategoryMapper.INSTANCE::categoryToCategoryDTO);
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Category category = findCategoryById(id);
        return CategoryMapper.INSTANCE.categoryToCategoryDTO(category);
    }

    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {

        if(categoryRepository.existsByCategoryName(categoryDTO.getCategoryName())){
            throw new CustomException("Category already exists with name: " + categoryDTO.getCategoryName(), HttpStatus.BAD_REQUEST);
        }

        Category category = CategoryMapper.INSTANCE.categoryDTOToCategory(categoryDTO);
        categoryRepository.save(category);
        return CategoryMapper.INSTANCE.categoryToCategoryDTO(category);
    }

    @Override
    public void updateCategory(Long id, CategoryDTO updatedCategoryDTO) {
        Category category = findCategoryById(id);

        if (category.getDeletedAt() != null){
            throw new CustomException("Category is in trash with id: " + id, HttpStatus.BAD_REQUEST);
        }

        if (categoryRepository.existsByCategoryName(updatedCategoryDTO.getCategoryName()) && !updatedCategoryDTO.getCategoryName().equals(category.getCategoryName())) {
            throw new CustomException("Category already exists with name: " + updatedCategoryDTO.getCategoryName(), HttpStatus.BAD_REQUEST);
        }

        category.setCategoryName(updatedCategoryDTO.getCategoryName());
        category.setDescription(updatedCategoryDTO.getDescription());

        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        findCategoryById(id);
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryDTO> getCategoryByName(String name) {
        List<Category> categories = categoryRepository.findCategoriesByCategoryName(name);

        if(categories.isEmpty()){
                throw new CategoryNotFoundException("Category not found with name: " + name);
            }
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        for (Category category : categories) {
            categoryDTOS.add(CategoryMapper.INSTANCE.categoryToCategoryDTO(category));
        }
        return categoryDTOS;
    }

    public void moveToTrash(Long id) {
        Category category = findCategoryById(id);

        LocalDateTime now = LocalDateTime.now();
        category.setDeletedAt(now);
        categoryRepository.save(category);
    }

    @Override
    public Page<CategoryDTO> getInTrash(Pageable pageable) {
        Page<Category> categories = categoryRepository.findByDeletedAtIsNotNull(pageable);
        return categories.map(CategoryMapper.INSTANCE::categoryToCategoryDTO);
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