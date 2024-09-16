package com.example.productservice.controllers;

import com.example.productservice.dto.CategoryDTO;
import com.example.productservice.dto.ProductDTO;
import com.example.productservice.dto.response.ApiResponse;
import com.example.productservice.services.CategoryService;
import com.example.productservice.services.ProductService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.example.productservice.exception.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Valid
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    private final CategoryService categoryService;

    @GetMapping("/search-by-specification")
    ApiResponse<?> advanceSearchBySpecification(@RequestParam(defaultValue = "1", name = "page") int page,
                                                       @RequestParam(defaultValue = "10", name = "limit") int limit,
                                                        @RequestParam(required = false) String sort,
                                                        @RequestParam(required = false) String[] product,
                                                        @RequestParam(required = false) String category) {
        return ApiResponse.builder()
                .message("List of Products")
                .data(productService.searchBySpecification(PageRequest.of(page -1, limit), sort, product, category))
                .build();
    }

    @GetMapping("/getAll")
    ApiResponse<Page<ProductDTO>> getAllProducts(
            @RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "limit") int limit) {
        return ApiResponse.<Page<ProductDTO>>builder()
                .message("Get all Products")
                .data(productService.getAllProducts(PageRequest.of(page -1, limit)))
                .build();
    }

    @GetMapping("/count")
    ApiResponse<Long> countProducts() {
        return ApiResponse.<Long>builder()
                .message("Get count Products")
                .data(productService.countProducts())
                .build();
    }

    @GetMapping("/id/{id}")
    ApiResponse<?> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        if (product == null) {
            throw new NotFoundException("Product not found with id: " + id);
        }
        return ApiResponse.builder()
                .message("Get product by Id")
                .data(product)
                .build();
    }

    @PostMapping("/list")
    ApiResponse<?> getProductsByIds(@RequestBody Set<Long> productIds) {
        return ApiResponse.builder()
                .message("Get products by Ids")
                .data(productService.getProductsByIds(productIds))
                .build();
    }

    @GetMapping("/name/{name}")
    ApiResponse<?> getProductByName(@PathVariable String name) {
        ProductDTO product = productService.getProductByName(name);
        if (product == null) {
            throw new NotFoundException("Product not found with name: " + name);
        }
        return ApiResponse.builder()
                .message("Get product by Name")
                .data(product)
                .build();
    }

    @GetMapping("/category/{categoryId}")
    ApiResponse<Page<ProductDTO>> findByCategory( @RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int limit,
                                            @PathVariable Long categoryId) {
        CategoryDTO category = categoryService.getCategoryById(categoryId);
        return ApiResponse.<Page<ProductDTO>>builder()
                .message("Get products by Category")
                .data(productService.findByCategory(PageRequest.of(page -1, limit), category))
                .build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> addProduct(@Valid @RequestPart("productDTO") ProductDTO productDTO, @RequestPart("files") @NonNull List<MultipartFile> imageFiles, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Error")
                    .data(errors)
                    .build());
        }
        productService.addProduct(productDTO, imageFiles);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(HttpStatus.CREATED.value())
                .message("Create product successfully")
                .build());
    }
    @PutMapping("/{id}")
    ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestPart("productDTO") ProductDTO updatedProductDto, @RequestPart("files") @NonNull List<MultipartFile> imageFiles, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Error")
                    .data(errors)
                    .build());
        }
        productService.updateProduct(id, updatedProductDto, imageFiles);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Update product successfully")
                .build());
    }

    @DeleteMapping("/in-trash/{id}")
    ApiResponse<?> moveToTrash(@PathVariable Long id) {
        productService.moveToTrash(id);
        return ApiResponse.builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Move to trash product successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ApiResponse.builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Delete Product Successfully")
                .build();
    }

    @GetMapping("/trash")
    ApiResponse<?> getInTrashProduct(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "limit", defaultValue = "10") int limit){
        return ApiResponse.builder()
                .message("Get in trash Product")
                .data(productService.getInTrash(PageRequest.of(page -1, limit)))
                .build();
    }

    @PutMapping("/updateQuantity/{id}")
    ApiResponse<?> updateStockQuantity(@PathVariable Long id, @RequestParam Integer quantity) {
        productService.updateStockQuantity(id, quantity);
        return ApiResponse.builder()
                .message("Update stock quantity successfully")
                .build();
    }

    @PutMapping("/restore/{id}")
    ApiResponse<?> restoreProduct(@PathVariable Long id) {
        productService.restoreProduct(id);
        return ApiResponse.builder()
                .message("Restore product successfully")
                .build();
    }
}
