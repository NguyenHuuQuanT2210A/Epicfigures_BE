package com.example.productservice.controllers;

import com.example.productservice.dto.FileUploadedDTO;
import com.example.productservice.dto.ProductImageDTO;
import com.example.productservice.dto.response.ApiResponse;
import com.example.productservice.exception.CustomException;
import com.example.productservice.services.FileStorageService;
import com.example.productservice.services.FileUploadService;
import com.example.productservice.services.ProductImageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product-images")
public class ProductImageController {
    private final ProductImageService productImageSevice;
    private final FileStorageService fileStorageService;
    private final FileUploadService fileUploadService;

    @GetMapping("/productId/{productId}")
    ApiResponse<List<ProductImageDTO>> getProductImages(@PathVariable Long productId) {
        return ApiResponse.<List<ProductImageDTO>>builder()
                .message("Get all Product Images By Product ID")
                .data(productImageSevice.getProductImages(productId))
                .build();
    }

    @DeleteMapping("/{id}")
    void deleteProductImage(@PathVariable Long id) {
        productImageSevice.deleteProductImage(id);
    }

    @DeleteMapping("/product/{productId}")
    void deleteProductImages(@PathVariable Long productId) {
        productImageSevice.deleteProductImages(productId);
    }

    @DeleteMapping("/products")
    void deleteProductImages(@RequestParam List<Long> productIds) {
        productImageSevice.deleteProductImages(productIds);
    }

    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<List<ProductImageDTO>> saveProductImage(@RequestParam Long productId, @RequestParam("files") List<MultipartFile> imageFiles){
        return ApiResponse.<List<ProductImageDTO>>builder()
                .message("Create a new Product Image")
                .data(productImageSevice.saveProductImage(productId, imageFiles))
                .build();
    }

    @PutMapping
    ApiResponse<List<ProductImageDTO>> updateProductImage(@RequestParam Long productId, @RequestParam List<Long> productImageIds, @RequestParam("files") List<MultipartFile> imageFiles) {
        return ApiResponse.<List<ProductImageDTO>>builder()
                .message("Update Product Image")
                .data(productImageSevice.updateProductImage(productId, productImageIds, imageFiles))
                .build();
    }

    @GetMapping("/imagesPost/{filename:.+}")
    ResponseEntity<?> downloadFile(@PathVariable String filename, HttpServletRequest request){
        Resource resource = fileStorageService.loadProductImageFileAsResource(filename);

        String contentType;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        }catch (Exception ex){
            throw new CustomException("File not found" + ex, HttpStatus.NOT_FOUND);
        }
        if (contentType == null){
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=\""
                        + resource.getFilename() + "\"")
                .body(resource);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping ("/images/{filename:.+}")
    ResponseEntity<?> deleteFile(@PathVariable String filename){
        fileStorageService.deleteProductImageFile(filename);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("delete images successfully")
                .build());
    }

    @PostMapping("/upload")
    FileUploadedDTO uploadImageCloudinary(@RequestParam("file") MultipartFile file) throws Exception {
        return fileUploadService.uploadFile(file);
    }
}
