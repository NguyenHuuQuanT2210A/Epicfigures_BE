package com.example.productservice.controllers;

import com.example.productservice.dto.CategoryDTO;
import com.example.productservice.exception.CategoryNotFoundException;
import com.example.productservice.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
@CrossOrigin()
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> getAllCategory(@RequestParam(defaultValue = "1", name = "page") int page, @RequestParam(defaultValue = "10", name = "limit") int limit) {
        Page<CategoryDTO> categoryDTOS = categoryService.getAllCategory(PageRequest.of(page - 1, limit));
        return ResponseEntity.ok(categoryDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        CategoryDTO category = categoryService.getCategoryById(id);
        if (category == null) {
            throw new CategoryNotFoundException("Category not found with id: " + id);
        }
        return ResponseEntity.ok(category);
    }

    @GetMapping("/name/{name}")
    public List<CategoryDTO> getCategoryByName(@PathVariable String name) {
        List<CategoryDTO> category = categoryService.getCategoryByName(name);
        if (category == null) {
            throw new CategoryNotFoundException("Category not found with name: " + name);
        }
        return category;
    }

    @PostMapping
    public ResponseEntity<?> addCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        categoryService.addCategory(categoryDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        categoryService.updateCategory(id, categoryDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> moveToTrash(@PathVariable Long id) {
        categoryService.moveToTrash(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/in-trash/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/trash")
    public ResponseEntity<?> getInTrashCategory(@RequestParam(defaultValue = "1", name = "page") int page, @RequestParam(defaultValue = "10", name = "limit") int limit) {
        return ResponseEntity.ok(categoryService.getInTrash(PageRequest.of(page - 1, limit)));
    }
}
