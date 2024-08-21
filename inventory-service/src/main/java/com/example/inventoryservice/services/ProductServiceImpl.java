//package com.example.inventoryservice.services;
//
//import com.example.productservice.dto.CategoryDTO;
//import com.example.productservice.dto.ProductDTO;
//import com.example.productservice.entities.Category;
//import com.example.productservice.entities.Product;
//import com.example.productservice.exception.NotFoundException;
//import com.example.productservice.mapper.CategoryMapper;
//import com.example.productservice.mapper.ProductMapper;
//import com.example.productservice.repositories.ProductRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class ProductServiceImpl implements ProductService {
//
//    private final ProductRepository productRepository;
//
//    private final CategoryService categoryService;
//
//    private final ProductMapper productMapper;
//
//    private final CategoryMapper categoryMapper;
//
//    @Override
//    public Page<ProductDTO> getAllProducts(Pageable pageable) {
//        Page<Product> products = productRepository.findByDeletedAtIsNull(pageable);
//        return products.map(productMapper.INSTANCE::productToProductDTO);
//    }
//
//    @Override
//    public ProductDTO getProductByName(String name) {
//        Product product = productRepository.findByNameAndDeletedAtIsNull(name);
//        if (product == null) {
//            throw new NotFoundException("Product not found with name: " + name);
//        }
//        return productMapper.INSTANCE.productToProductDTO(product);
//    }
//
//    @Override
//    public void addProduct(ProductDTO productDTO) {
//        if (productRepository.existsByName(productDTO.getName())) {
//            throw new RuntimeException("Product already exists with name: " + productDTO.getName());
//        }
//
//        CategoryDTO categoryDTO = categoryService.getCategoryById(productDTO.getCategoryId());
//        if (categoryDTO == null) {
//            throw new RuntimeException("Can not find category with id " + productDTO.getCategoryId());
//        }
//
//        Product product = productMapper.INSTANCE.productDTOToProduct(productDTO);
//
//        product.setCategory(categoryMapper.INSTANCE.categoryDTOToCategory(categoryDTO));
//
//        productRepository.save(product);
//    }
//
//    @Override
//    public Page<ProductDTO> findByCategory(Pageable pageable, CategoryDTO categoryDTO) {
//        CategoryDTO category = categoryService.getCategoryById(categoryDTO.getCategoryId());
//        if (category == null) {
//            throw new RuntimeException("Can not find category with id " + categoryDTO.getCategoryId());
//        }
//        return productRepository.findByCategoryAndDeletedAtIsNull(pageable, categoryMapper.INSTANCE.categoryDTOToCategory(category))
//                .map(productMapper.INSTANCE::productToProductDTO);
//    }
//
//    @Override
//    public void updateProduct(long id, ProductDTO updatedProductDTO) {
//        Optional<Product> existingProduct = productRepository.findById(id);
//        if (existingProduct.isEmpty()) {
//            throw new RuntimeException("Can not find product with id" + id);
//        }
//        if (updatedProductDTO.getPrice() != null) {
//            existingProduct.get().setPrice(updatedProductDTO.getPrice());
//        }
//        if (updatedProductDTO.getName() != null) {
//            if (productRepository.existsByName(updatedProductDTO.getName())) {
//                throw new RuntimeException("Product already exists with name: " + updatedProductDTO.getName());
//            }
//            existingProduct.get().setName(updatedProductDTO.getName());
//        }
//        if (updatedProductDTO.getDescription() != null) {
//            existingProduct.get().setDescription(updatedProductDTO.getDescription());
//        }
//
////        if (updatedProductDTO.getImages() != null) {
////            Set<ProductImageDTO> images = updatedProductDTO.getImages();
////            existingProduct.get().setImages(productMapper.INSTANCE.productImageDTOSetToProductImageSet(images));
////        }
//
////   not do this     if (updatedProductDTO.getStockQuantity() != null) {
////            existingProduct.get().setStockQuantity(updatedProductDTO.getStockQuantity());
////        }
//
//        if (updatedProductDTO.getManufacturer() != null) {
//            existingProduct.get().setManufacturer(updatedProductDTO.getManufacturer());
//        }
//
//        if (updatedProductDTO.getSize() != null) {
//            existingProduct.get().setSize(updatedProductDTO.getSize());
//        }
//
//        if (updatedProductDTO.getWeight() != null) {
//            existingProduct.get().setWeight(updatedProductDTO.getWeight());
//        }
//
//        if (updatedProductDTO.getCategoryId() != null) {
//            CategoryDTO categoryDTO = categoryService.getCategoryById(updatedProductDTO.getCategoryId());
//            if (categoryDTO == null) {
//                throw new RuntimeException("Can not find category with id " + updatedProductDTO.getCategoryId());
//            }
//
//            Category category = categoryMapper.INSTANCE.categoryDTOToCategory(categoryDTO);
//            existingProduct.get().setCategory(category);
//        }
//        productRepository.save(existingProduct.get());
//    }
//
//    @Override
//    public void deleteProduct(long id) {
//        productRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
//
//        productRepository.deleteById(id);
//    }
//
//    @Override
//    public void moveToTrash(Long id) {
//        Product product = productRepository.findById(id).orElse(null);
//        if (product == null) {
//            throw new NotFoundException("Cannot find this product id: " + id);
//        }
//        LocalDateTime now = LocalDateTime.now();
//        product.setDeletedAt(now);
//        productRepository.save(product);
//    }
//
//    @Override
//    public Page<ProductDTO> getInTrash(Pageable pageable) {
//        Page<Product> products = productRepository.findByDeletedAtIsNotNull(pageable);
//        return products.map(productMapper.INSTANCE::productToProductDTO);
//    }
//}