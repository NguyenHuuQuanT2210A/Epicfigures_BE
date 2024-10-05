package com.example.productservice.controllers;

import com.example.productservice.dto.request.CategoryRequest;
import com.example.productservice.dto.response.ApiResponse;
import com.example.productservice.dto.response.CategoryResponse;
import com.example.productservice.exception.CategoryNotFoundException;
import com.example.productservice.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/getAll")
    ApiResponse<Page<CategoryResponse>> getAllCategory(@RequestParam(defaultValue = "1", name = "page") int page, @RequestParam(defaultValue = "10", name = "limit") int limit) {
        Page<CategoryResponse> categoryDTOS = categoryService.getAllCategories(PageRequest.of(page - 1, limit, Sort.by("createdAt").descending()));
        return ApiResponse.<Page<CategoryResponse>>builder()
                .message("Get all Category")
                .data(categoryDTOS)
                .build();
    }

    @GetMapping("/count")
    ApiResponse<Long> countCategories() {
        return ApiResponse.<Long>builder()
                .message("Get count Categories")
                .data(categoryService.countCategories())
                .build();
    }

    @GetMapping("/id/{id}")
    ApiResponse<CategoryResponse> getCategoryById(@PathVariable Long id) {
        CategoryResponse category = categoryService.getCategoryById(id);
        if (category == null) {
            throw new CategoryNotFoundException("Category not found with id: " + id);
        }
        return ApiResponse.<CategoryResponse>builder()
                .message("Get Category By Id")
                .data(category)
                .build();
    }

    @GetMapping("/name/{name}")
    ApiResponse<Page<CategoryResponse>> getCategoryByName(@RequestParam(defaultValue = "1", name = "page") int page,
                                                          @RequestParam(defaultValue = "10", name = "limit") int limit,
                                                          @PathVariable String name) {
        return ApiResponse.<Page<CategoryResponse>>builder()
                .message("Get Categories By Name")
                .data(categoryService.getCategoryByName(name, PageRequest.of(page - 1, limit, Sort.by("createdAt").descending())))
                .build();
    }

    @GetMapping("/parentCategory/{parentCategoryId}")
    ApiResponse<Page<CategoryResponse>> getCategoryByParentCategoryId(@RequestParam(defaultValue = "1", name = "page") int page,
                                                                      @RequestParam(defaultValue = "10", name = "limit") int limit,
                                                                      @PathVariable Long parentCategoryId) {
        return ApiResponse.<Page<CategoryResponse>>builder()
                .message("Get Category By Parent Category Id")
                .data(categoryService.getCategoryByParentCategoryId(parentCategoryId, PageRequest.of(page - 1, limit, Sort.by("createdAt").descending())))
                .build();
    }

    @GetMapping("/parentCategoryIsNull")
    ApiResponse<Page<CategoryResponse>> getCategoryByParentCategoryIsNull(@RequestParam(defaultValue = "1", name = "page") int page,
                                                                          @RequestParam(defaultValue = "10", name = "limit") int limit) {
        return ApiResponse.<Page<CategoryResponse>>builder()
                .message("Get Category By Parent Category IS Null")
                .data(categoryService.getCategoriesByParentCategoryIsNull(PageRequest.of(page - 1, limit, Sort.by("createdAt").descending())))
                .build();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    ResponseEntity<?> addCategory(@Valid @RequestBody CategoryRequest request, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message("Error: " + errors.get("field"))
                    .data(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors))
                    .build());
        }
        return ResponseEntity.ok(ApiResponse.builder()
                .code(HttpStatus.CREATED.value())
                .message("Created Category Successfully")
                .data(categoryService.addCategory(request))
                .build());
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message("Error: " + errors.get("field"))
                    .data(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors))
                    .build());
        }
        categoryService.updateCategory(id, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Update Category Successfully")
                .build());
    }

    @DeleteMapping("/{id}")
    ApiResponse<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ApiResponse.builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Delete Category Successfully")
                .build();
    }

    @DeleteMapping("/in-trash/{id}")
    ApiResponse<?> moveToTrash(@PathVariable Long id) {
        categoryService.moveToTrash(id);
        return ApiResponse.builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Move to Trash Category Successfully")
                .build();
    }

    @GetMapping("/trash")
    ApiResponse<?> getInTrashCategory(@RequestParam(defaultValue = "1", name = "page") int page, @RequestParam(defaultValue = "10", name = "limit") int limit) {
        return ApiResponse.builder()
                .message("Get In Trash Category")
                .data(categoryService.getInTrash(PageRequest.of(page - 1, limit)))
                .build();
    }

    @PutMapping("/restore/{id}")
    ApiResponse<?> restoreCategory(@PathVariable Long id) {
        categoryService.restoreCategory(id);
        return ApiResponse.builder()
                .message("Restore Category Successfully")
                .build();
    }
}
