package com.example.productservice.services;

import com.example.productservice.dto.ProductDTO;
import com.example.productservice.dto.ProductImageDTO;
import com.example.productservice.entities.Product;
import com.example.productservice.entities.ProductImage;
import com.example.productservice.exception.CustomException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.mapper.ProductImageMapper;
import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.repositories.ProductImageRepository;
import com.example.productservice.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
    private final FileStorageService fileStorageService;

    @Override
    public void deleteProductImage(Long id) {
        var productImage = productImageRepository.findById(id);
        if (productImage.isEmpty()) {
            throw new CustomException("Product image not found with id: " + id, HttpStatus.BAD_REQUEST);
        }
        fileStorageService.deleteProductImageFile(productImage.get().getImageUrl());
        productImageRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteProductImages(Long productId) {
        ProductDTO product = getProductById(productId);
        if (product == null) {
            throw new CustomException("Product not found with id: " + productId, HttpStatus.BAD_REQUEST);
        }
        List<ProductImage> productImages = productImageRepository.findByProductProductId(productId);
        for (ProductImage productImage : productImages) {
            fileStorageService.deleteProductImageFile(productImage.getImageUrl());
        }
        productImageRepository.deleteAllImagesByProductId(productId);
    }

    @Override
    @Transactional
    public void deleteProductImages(List<Long> productIds) {
        for (Long productId : productIds) {
            ProductDTO product = getProductById(productId);
            if (product == null) {
                throw new CustomException("Product not found with id: " + productId, HttpStatus.BAD_REQUEST);
            }
            List<ProductImage> productImages = productImageRepository.findByProductProductId(productId);
            for (ProductImage productImage : productImages) {
                if (!productImage.getProduct().getProductId().equals(productId)) {
                    throw new CustomException("Product image not found with id: " + productImage.getImageId() + " for product id: " + productId, HttpStatus.BAD_REQUEST);
                }
                fileStorageService.deleteProductImageFile(productImage.getImageUrl());
            }
        }
        productImageRepository.deleteAllImagesByProductIds(productIds);
    }

    @Override
    public List<ProductImageDTO> getProductImages(Long productId) {
        ProductDTO product = getProductById(productId);
        if (product == null) {
            throw new CustomException("Product not found with id: " + productId, HttpStatus.BAD_REQUEST);
        }
        List<ProductImage> productImages = productImageRepository.findByProductProductId(productId);

        return productImages.stream().map(productImageMapper.INSTANCE::productImageToProductImageDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ProductImageDTO> saveProductImage(Long productId, List<MultipartFile> imageFiles) {
        ProductDTO product = getProductById(productId);
        if (imageFiles == null || imageFiles.isEmpty()) {
            throw new CustomException("Image files are required", HttpStatus.BAD_REQUEST);
        }

        if (product == null) {
            throw new CustomException("Product not found with id: " + productId, HttpStatus.BAD_REQUEST);
        }

        List<ProductImageDTO> productImageDTOs = new ArrayList<>();
        for (MultipartFile imageFile : imageFiles) {
            ProductImage productImageEntity = new ProductImage();
            productImageEntity.setImageUrl(fileStorageService.storeProductImageFile(imageFile));
            productImageEntity.setProduct(productMapper.INSTANCE.productDTOToProduct(product));
            productImageRepository.save(productImageEntity);
            productImageDTOs.add(productImageMapper.INSTANCE.productImageToProductImageDTO(productImageEntity));
        }
        return productImageDTOs;
    }

    @Override
    @Transactional
    public List<ProductImageDTO> updateProductImage(Long productId, List<Long> productImageIds, List<MultipartFile> imageFiles) {
        ProductDTO product = getProductById(productId);

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
            productImageRepository.deleteById(productImageId);
        }

        // Add new product images
        var productImages = productImageRepository.findByProductProductId(product.getProductId()).stream().map(productImageMapper::productImageToProductImageDTO).toList();
        List<ProductImageDTO> updatedImages = new ArrayList<>(productImages);

        if (imageFiles.size() == 1 && Objects.equals(imageFiles.get(0).getOriginalFilename(), "")){
            return updatedImages;
        }

        for (MultipartFile imageFile : imageFiles) {
            ProductImage productImageEntity = new ProductImage();
            productImageEntity.setProduct(productMapper.INSTANCE.productDTOToProduct(product));
            productImageEntity.setImageUrl(fileStorageService.storeProductImageFile(imageFile));
            ProductImage updatedProductImage = productImageRepository.save(productImageEntity);
            updatedImages.add(productImageMapper.INSTANCE.productImageToProductImageDTO(updatedProductImage));
        }
        return updatedImages;
    }

    private ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
        return productMapper.INSTANCE.productToProductDTO(product);
    }
}
