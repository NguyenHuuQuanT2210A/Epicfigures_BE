package com.example.productservice.services.impl;

import com.example.productservice.dto.request.ProductQuantityRequest;
import com.example.productservice.dto.response.ProductQuantityResponse;
import com.example.productservice.entities.Product;
import com.example.productservice.entities.ProductQuantity;
import com.example.productservice.exception.CustomException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.mapper.ProductQuantityMapper;
import com.example.productservice.repositories.ProductQuantityRepository;
import com.example.productservice.repositories.ProductRepository;
import com.example.productservice.services.ProductQuantityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductQuantityServiceImpl implements ProductQuantityService {

    private final ProductQuantityRepository productQuantityRepository;
    private final ProductRepository productRepository;

    @Override
    public Page<ProductQuantityResponse> getAllCategories(Pageable pageable) {
        Page<ProductQuantity> categories = productQuantityRepository.findAll(pageable);
        return categories.map(ProductQuantityMapper.INSTANCE::toProductQuantityResponse);
    }

    @Override
    public ProductQuantityResponse getProductQuantityById(Long id) {
        return ProductQuantityMapper.INSTANCE.toProductQuantityResponse(findProductQuantityById(id));
    }

    @Override
    public Long addProductQuantity(ProductQuantityRequest request) {
        var product = getProductById(request.getProductId());
        if (productQuantityRepository.existsByProduct(product)) {
            throw new CustomException("ProductQuantity already exists for product with id: " + request.getProductId(), HttpStatus.BAD_REQUEST);
        }
        return productQuantityRepository.save(ProductQuantity.builder()
                        .stockQuantity(0L)
                        .reservedQuantity(0L)
                        .soldQuantity(0L)
                .product(product).build()).getId();
    }

    @Override
    public void updateProductQuantity(Long id, ProductQuantityRequest request) {
        ProductQuantity productQuantity = findProductQuantityById(id);
        ProductQuantityMapper.INSTANCE.updatedProductQuantity(productQuantity, request);
        productQuantityRepository.save(productQuantity);
    }

    @Override
    public void deleteProductQuantity(Long id) {
        findProductQuantityById(id);
        productQuantityRepository.deleteById(id);
    }

    @Override
    public ProductQuantityResponse getProductQuantityByProductId(Long productId) {
        var product = getProductById(productId);
        return ProductQuantityMapper.INSTANCE.toProductQuantityResponse(productQuantityRepository.findByProduct(product));
    }

    private ProductQuantity findProductQuantityById(Long id) {
        return productQuantityRepository.findById(id).orElseThrow(() -> new CustomException("ProductQuantity not found with id: " + id, HttpStatus.NOT_FOUND));
    }

    private Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
    }
}