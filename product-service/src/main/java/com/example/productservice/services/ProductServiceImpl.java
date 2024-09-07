package com.example.productservice.services;

import com.example.productservice.dto.CategoryDTO;
import com.example.productservice.dto.ProductDTO;
import com.example.productservice.dto.ProductImageDTO;
import com.example.productservice.entities.Category;
import com.example.productservice.entities.Product;
import com.example.productservice.exception.CategoryNotFoundException;
import com.example.productservice.exception.CustomException;
import com.example.productservice.mapper.CategoryMapper;
import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.repositories.ProductRepository;
import com.example.productservice.services.impl.BaseRedisServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

import static com.example.productservice.constant.CommonDefine.*;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final ProductImageService productImageService;
    private final RestTemplate restTemplate;
    private final BaseRedisServiceImpl<String, String, Object> redisService;
    private final ObjectMapper objectMapper;


    private ProductDTO convertToProductDTO(Object object) {
        if (object instanceof LinkedHashMap) {
            return objectMapper.convertValue(object, ProductDTO.class);
        } else {
            return (ProductDTO) object;
        }
    }

    @Override
    public int countProducts() {
        return (int) productRepository.count();
    }

    @Override
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        String key = String.format(GET_ALL_PRODUCTS, pageable.getPageNumber(), pageable.getPageSize());

        //redis
        if (redisService.keyExists(key)) {
//            Map<String, Object> productsMap = redisService.getField(key);
//
//            for (Map.Entry<String, Object> entry : productsMap.entrySet()) {
//                Map<String, Object> product = (Map<String, Object>) entry.getValue();
//                    productDTOS.add(convertToProductDTO(product));
//            }

            List<Object> values = redisService.getList(key);
            List<ProductDTO> productDTOS = new ArrayList<>();
            for (Object value : values) {
                productDTOS.add(convertToProductDTO(value));
            }
            return new PageImpl<>(productDTOS, pageable, productDTOS.size());
        } else {
            Page<Product> products = productRepository.findByDeletedAtIsNull(pageable);
            return products.map(product -> {
                ProductDTO productDTO = productMapper.INSTANCE.productToProductDTO(product);
                redisService.rightPushAll(key, Collections.singletonList(productDTO));
//                redisService.hashSet(key, PRODUCT_ID + product.getProductId(), productDTO);
                return productDTO;
            });
        }
    }

    @Override
    public ProductDTO getProductByName(String name) {
        Product product = productRepository.findByNameAndDeletedAtIsNull(name);
        if (product == null) {
            throw new CustomException("Product not found with name: " + name, HttpStatus.BAD_REQUEST);
        }
        return productMapper.INSTANCE.productToProductDTO(product);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        if (redisService.keyExists(PRODUCT_ID + id)) {
            Object object =  redisService.getField(PRODUCT_ID + id);
            return convertToProductDTO(object);
        }
        Product product = findProductById(id);
        var productResponse = productMapper.INSTANCE.productToProductDTO(product);
        //redis
        redisService.hashSetAll(PRODUCT_ID + id, productResponse);
        return productResponse;
    }

    @Override
    public List<ProductDTO> getProductsByIds(Set<Long> productIds) {
        List<Product> products = productRepository.findByProductIdIn(productIds);
        products.forEach(product -> {
            if (product.getProductId() == null) {
                throw new CustomException("Product is not found", HttpStatus.NOT_FOUND);
            }
        });

        return productMapper.INSTANCE.productListToProductDTOList(products);
    }

    @Override
    public void addProduct(ProductDTO productDTO, List<MultipartFile> imageFiles) {
        if (productRepository.existsByName(productDTO.getName())) {
            throw new CustomException("Product already exists with name: " + productDTO.getName(), HttpStatus.CONFLICT);
        }

        CategoryDTO categoryDTO = categoryService.getCategoryById(productDTO.getCategoryId());
        if (categoryDTO == null) {
            throw new CustomException("Can not find category with id " + productDTO.getCategoryId(), HttpStatus.NOT_FOUND);
        }

        Product product = productMapper.INSTANCE.productDTOToProduct(productDTO);

        product.setCategory(categoryMapper.INSTANCE.categoryDTOToCategory(categoryDTO));

        productRepository.save(product);

        productImageService.saveProductImage(product.getProductId(), imageFiles);

        //redis
        redisService.getKeyPrefixes("get_products" + "*").forEach(redisService::delete);
    }

    @Override
    public Page<ProductDTO> findByCategory(Pageable pageable, CategoryDTO categoryDTO) {
        CategoryDTO category = categoryService.getCategoryById(categoryDTO.getCategoryId());

        String key = String.format(GET_PRODUCTS_BY_CATEGORY, category.getCategoryId(), pageable.getPageNumber(), pageable.getPageSize());

        if (redisService.keyExists(key)) {
            List<Object> values = redisService.getList(key);
            List<ProductDTO> productDTOS = new ArrayList<>();
            for (Object value : values) {
                productDTOS.add(convertToProductDTO(value));
            }
            return new PageImpl<>(productDTOS, pageable, productDTOS.size());
        }else {
            if (category == null) {
                throw new CustomException("Can not find category with id " + categoryDTO.getCategoryId(), HttpStatus.NOT_FOUND);
            }
            return productRepository.findByCategoryAndDeletedAtIsNull(pageable, categoryMapper.INSTANCE.categoryDTOToCategory(category))
                    .map(product -> {
                        ProductDTO productDTO = productMapper.INSTANCE.productToProductDTO(product);
                        //redis
                        redisService.rightPushAll(key, Collections.singletonList(productDTO));
                        return productDTO;
                    });
        }
    }

    @Override
    public void updateProduct(long id, ProductDTO updatedProductDTO, List<MultipartFile> imageFiles) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isEmpty()) {
            throw new CustomException("Can not find product with id" + id, HttpStatus.NOT_FOUND);
        }
        if (updatedProductDTO.getPrice() != null) {
            existingProduct.get().setPrice(updatedProductDTO.getPrice());
        }
        if (updatedProductDTO.getName() != null) {
            if (productRepository.existsByName(updatedProductDTO.getName()) && !updatedProductDTO.getName().equals(existingProduct.get().getName())) {
                throw new CustomException("Product already exists with name: " + updatedProductDTO.getName(), HttpStatus.BAD_REQUEST);
            }
            existingProduct.get().setName(updatedProductDTO.getName());
        }
        if (updatedProductDTO.getDescription() != null) {
            existingProduct.get().setDescription(updatedProductDTO.getDescription());
        }

//        if (updatedProductDTO.getImages() != null) {
//            Set<ProductImageDTO> images = updatedProductDTO.getImages();
//            existingProduct.get().setImages(productMapper.INSTANCE.productImageDTOSetToProductImageSet(images));
//        }

//   not do this     if (updatedProductDTO.getStockQuantity() != null) {
//            existingProduct.get().setStockQuantity(updatedProductDTO.getStockQuantity());
//        }

        if (updatedProductDTO.getManufacturer() != null) {
            existingProduct.get().setManufacturer(updatedProductDTO.getManufacturer());
        }

        if (updatedProductDTO.getSize() != null) {
            existingProduct.get().setSize(updatedProductDTO.getSize());
        }

        if (updatedProductDTO.getWeight() != null) {
            existingProduct.get().setWeight(updatedProductDTO.getWeight());
        }

        if (updatedProductDTO.getCategoryId() != null) {
            CategoryDTO categoryDTO = categoryService.getCategoryById(updatedProductDTO.getCategoryId());
            if (categoryDTO == null) {
                throw new CategoryNotFoundException("Can not find category with id " + updatedProductDTO.getCategoryId());
            }

            Category category = categoryMapper.INSTANCE.categoryDTOToCategory(categoryDTO);
            existingProduct.get().setCategory(category);
        }

        existingProduct.get().setStockQuantity(existingProduct.get().getStockQuantity());
        productRepository.save(existingProduct.get());

        List<Long> productImageIds = new ArrayList<>();
        List<ProductImageDTO> productImageDTOs = productImageService.getProductImages(existingProduct.get().getProductId());
        for (ProductImageDTO productImageDTO : productImageDTOs) {
            if (!updatedProductDTO.getImages().contains(productImageDTO)) {
                productImageIds.add(productImageDTO.getImageId());
            }
        }
        productImageService.updateProductImage(existingProduct.get().getProductId(), productImageIds , imageFiles);

        //redis
        if (redisService.keyExists(PRODUCT_ID + id)) {
            redisService.delete(PRODUCT_ID + id);
        }
        redisService.getKeyPrefixes("get_products" + "*").forEach(redisService::delete);
    }

    @Override
    public void updateStockQuantity(long id, Integer stockQuantity) {
        Product product = findProductById(id);
        redisService.delete(PRODUCT_ID + id);

        product.setStockQuantity(stockQuantity);
        productRepository.save(product);

        //redis
        redisService.hashSetAll(PRODUCT_ID + id, productMapper.INSTANCE.productToProductDTO(product));
    }

    @Override
    public void deleteProduct(long id) {
        findProductById(id);
        productRepository.deleteById(id);
        //redis
        redisService.delete(PRODUCT_ID + id);
        redisService.getKeyPrefixes("get_products" + "*").forEach(redisService::delete);
    }

    @Override
    public void moveToTrash(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            throw new CustomException("Cannot find this product id: " + id, HttpStatus.NOT_FOUND);
        }
        LocalDateTime now = LocalDateTime.now();
        product.setDeletedAt(now);
        productRepository.save(product);
    }

    @Override
    public Page<ProductDTO> getInTrash(Pageable pageable) {
        Page<Product> products = productRepository.findByDeletedAtIsNotNull(pageable);
        return products.map(productMapper.INSTANCE::productToProductDTO);
    }

    private Product findProductById(long id) {
        //có thể sử dụng Distributed Lock
        return productRepository.findById(id).orElseThrow(() -> new CustomException("Product not found with id: " + id, HttpStatus.BAD_REQUEST));
    }
}