package com.example.productservice.services;

import com.example.productservice.dto.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    Page<CategoryDTO> getAllCategory(Pageable pageable);
    CategoryDTO getCategoryById(Long id);
    CategoryDTO addCategory(CategoryDTO categoryDTO);
    void updateCategory(Long id, CategoryDTO categoryDTO);
    void deleteCategory(Long id);
    List<CategoryDTO> getCategoryByName(String name);
    void moveToTrash(Long id);
    Page<CategoryDTO> getInTrash(Pageable pageable);

    void restoreCategory(Long id);

    Long countCategories();
}
