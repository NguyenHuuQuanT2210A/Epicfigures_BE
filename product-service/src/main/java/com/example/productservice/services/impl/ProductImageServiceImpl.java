package com.example.productservice.services.impl;

import com.example.productservice.configs.FirebaseStorageProperties;
import com.example.productservice.dto.response.ProductImageResponse;
import com.example.productservice.dto.response.ProductResponse;
import com.example.productservice.entities.Product;
import com.example.productservice.entities.ProductImage;
import com.example.productservice.exception.CustomException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.mapper.ProductImageMapper;
import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.repositories.ProductImageRepository;
import com.example.productservice.repositories.ProductRepository;
import com.example.productservice.services.FileStorageService;
import com.example.productservice.services.FirebaseService;
import com.example.productservice.services.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final ProductImageMapper productImageMapper;
    private final ProductMapper productMapper;
//    private final FileStorageService fileStorageService;
    private final FirebaseService firebaseService;
    private final FirebaseStorageProperties firebaseStorageProperties;

    @Override
    public List<ProductImageResponse> getProductImages(Long productId) {
        ProductResponse product = getProductById(productId);
        if (product == null) {
            throw new CustomException("Product not found with id: " + productId, HttpStatus.BAD_REQUEST);
        }
        List<ProductImage> productImages = productImageRepository.findByProductProductId(productId);

        return productImages.stream().map(productImageMapper.INSTANCE::toProductImageResponse).collect(Collectors.toList());
    }

    @Override
    public List<ProductImageResponse> getProductImagesByProductId(Long productId) {
        ProductResponse product = getProductById(productId);
        if (product == null) {
            throw new CustomException("Product not found with id: " + productId, HttpStatus.BAD_REQUEST);
        }
        List<ProductImage> productImages = productImageRepository.findByProductProductId(productId);

        return productImages.stream()
                .sorted(Comparator.comparing(ProductImage::getCreatedAt)) // Sắp xếp theo createdAt
                .map(productImageMapper.INSTANCE::toProductImageResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductImageResponse> isProductImagesExist(List<Long> productImageIds) {
        var productImages = productImageRepository.findAll();

        //TH1
//        List<ProductImageDTO> result = new ArrayList<>();
//
//        for (Long productImageId : productImageIds) {
//            if (!productImageRepository.existsByImageId(productImageId)) {
//                ProductImageDTO productImageDTO = ProductImageDTO.builder()
//                        .imageId(productImageId)
//                        .isProductImageExist(false)
//                        .build();
//                result.add(productImageDTO);
//            }
//        }
//
//        for (var productImage : productImages) {
//            if (!productImageIds.contains(productImage.getImageId())) {
//                ProductImageDTO productImageDTO = ProductImageDTO.builder()
//                        .imageId(productImage.getImageId())
//                        .imageUrl(productImage.getImageUrl())
//                        .isProductImageExist(false)
//                        .build();
//                result.add(productImageDTO);
//            }
//        }

        //TH2
        for (Long productImageId : productImageIds) {
            for (var productImage : productImages) {
                if (!productImageRepository.existsByImageId(productImageId) || !productImageIds.contains(productImage.getImageId())) {
                    return productImages.stream()
                            .map(productImageMapper.INSTANCE::toProductImageResponse)
                            .collect(Collectors.toList());
                }
            }
        }

        return List.of();
    }

    @Override
    @Transactional
    public List<ProductImageResponse> saveProductImage(Long productId, List<MultipartFile> imageFiles) throws IOException {
        ProductResponse product = getProductById(productId);
        if (imageFiles == null || imageFiles.isEmpty()) {
            throw new CustomException("Image files are required", HttpStatus.BAD_REQUEST);
        }

        if (product == null) {
            throw new CustomException("Product not found with id: " + productId, HttpStatus.BAD_REQUEST);
        }

        List<ProductImageResponse> productImageDTOs = new ArrayList<>();
        for (MultipartFile imageFile : imageFiles) {
            ProductImage productImageEntity = new ProductImage();
//            productImageEntity.setImageUrl(fileStorageService.storeProductImageFile(imageFile));
            productImageEntity.setImageUrl(firebaseService.upload(imageFile, firebaseStorageProperties.getUploadProductImage()));

            productImageEntity.setProduct(productMapper.INSTANCE.productResponsetoProduct(product));
            productImageRepository.save(productImageEntity);
            productImageDTOs.add(productImageMapper.INSTANCE.toProductImageResponse(productImageEntity));
        }
        return productImageDTOs;
    }

    @Override
    @Transactional
    public List<ProductImageResponse> updateProductImage(Long productId, List<Long> productImageIds, List<MultipartFile> imageFiles) throws IOException {
        ProductResponse product = getProductById(productId);

        if (product == null) {
            throw new CustomException("Product not found with id: " + productId, HttpStatus.BAD_REQUEST);
        }

        // Delete old product images
        for (Long productImageId : productImageIds) {
            if (productImageRepository.findById(productImageId).isEmpty()) {
                throw new CustomException("Product image not found with id: " + productImageId, HttpStatus.BAD_REQUEST);
            }
            if (!productImageRepository.findById(productImageId).get().getProduct().getProductId().equals(productId)) {
                throw new CustomException("Product image not found with id: " + productImageId + " for product id: " + productId, HttpStatus.BAD_REQUEST);
            }
            deleteProductImage(productImageId);
        }

        // Add new product images
        var productImages = productImageRepository.findByProductProductId(product.getProductId()).stream().map(productImageMapper::toProductImageResponse).toList();
        List<ProductImageResponse> updatedImages = new ArrayList<>(productImages);

        if (imageFiles.size() == 1 && Objects.equals(imageFiles.get(0).getOriginalFilename(), "")){
            return updatedImages;
        }

        for (MultipartFile imageFile : imageFiles) {
            ProductImage productImageEntity = new ProductImage();
            productImageEntity.setProduct(productMapper.INSTANCE.productResponsetoProduct(product));
//            productImageEntity.setImageUrl(fileStorageService.storeProductImageFile(imageFile));
            productImageEntity.setImageUrl(firebaseService.upload(imageFile, firebaseStorageProperties.getUploadProductImage()));

            ProductImage updatedProductImage = productImageRepository.save(productImageEntity);
            updatedImages.add(productImageMapper.INSTANCE.toProductImageResponse(updatedProductImage));
        }
        return updatedImages;
    }

    @Override
    public void deleteProductImage(Long id) throws IOException {
        var productImage = productImageRepository.findById(id);
        if (productImage.isEmpty()) {
            throw new CustomException("Product image not found with id: " + id, HttpStatus.BAD_REQUEST);
        }
//        fileStorageService.deleteProductImageFile(productImage.get().getImageUrl());
        productImageRepository.deleteById(id);
        firebaseService.delete(productImage.get().getImageUrl());
    }

    @Override
    @Transactional
    public void deleteProductImages(Long productId) throws IOException {
        ProductResponse product = getProductById(productId);
        if (product == null) {
            throw new CustomException("Product not found with id: " + productId, HttpStatus.BAD_REQUEST);
        }
        List<ProductImage> productImages = productImageRepository.findByProductProductId(productId);
        for (ProductImage productImage : productImages) {
//            fileStorageService.deleteProductImageFile(productImage.getImageUrl());
            firebaseService.delete(productImage.getImageUrl());
        }
        productImageRepository.deleteAllImagesByProductId(productId);
    }

    @Override
    @Transactional
    public void deleteProductImages(List<Long> productIds) throws IOException {
        for (Long productId : productIds) {
            ProductResponse product = getProductById(productId);
            if (product == null) {
                throw new CustomException("Product not found with id: " + productId, HttpStatus.BAD_REQUEST);
            }
            List<ProductImage> productImages = productImageRepository.findByProductProductId(productId);
            for (ProductImage productImage : productImages) {
                if (!productImage.getProduct().getProductId().equals(productId)) {
                    throw new CustomException("Product image not found with id: " + productImage.getImageId() + " for product id: " + productId, HttpStatus.BAD_REQUEST);
                }
//                fileStorageService.deleteProductImageFile(productImage.getImageUrl());
                firebaseService.delete(productImage.getImageUrl());
            }
        }
        productImageRepository.deleteAllImagesByProductIds(productIds);
    }

    private ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
        return productMapper.INSTANCE.toProductResponse(product);
    }
}
