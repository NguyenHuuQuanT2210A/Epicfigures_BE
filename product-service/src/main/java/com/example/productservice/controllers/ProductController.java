package com.example.productservice.controllers;

import com.example.productservice.dto.CategoryDTO;
import com.example.productservice.dto.ProductDTO;
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

@CrossOrigin()
@Valid
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    private final CategoryService categoryService;

    @GetMapping
    public Page<ProductDTO> getAllProducts(
            @RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "limit") int limit) {
        return productService.getAllProducts(PageRequest.of(page -1, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        if (product == null) {
            throw new NotFoundException("Product not found with id: " + id);
        }
        return ResponseEntity.ok(product);
    }

    @PostMapping("/list")
    public ResponseEntity<?> getProductsByIds(@RequestBody Set<Long> productIds) {
        return ResponseEntity.ok(productService.getProductsByIds(productIds));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getProductByName(@PathVariable String name) {
        ProductDTO product = productService.getProductByName(name);
        if (product == null) {
            throw new NotFoundException("Product not found with name: " + name);
        }
        return ResponseEntity.ok(product);
    }
    @GetMapping("/category/{categoryId}")
    public Page<ProductDTO> findByCategory( @RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int limit,
                                            @PathVariable Long categoryId) {
        CategoryDTO category = categoryService.getCategoryById(categoryId);
        return productService.findByCategory(PageRequest.of(page -1, limit), category);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addProduct(@Valid @RequestPart("productDTO") ProductDTO productDTO, @RequestPart("files") @NonNull List<MultipartFile> imageFiles, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        productService.addProduct(productDTO, imageFiles);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestPart("productDTO") ProductDTO updatedProductDto, @RequestPart("files") @NonNull List<MultipartFile> imageFiles, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        productService.updateProduct(id, updatedProductDto, imageFiles);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> moveToTrash(@PathVariable Long id) {
        productService.moveToTrash(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/in-trash/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/trash")
    public ResponseEntity<?> getInTrashCategory(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "limit", defaultValue = "10") int limit){
        return ResponseEntity.ok(productService.getInTrash(PageRequest.of(page -1, limit)));
    }
}
