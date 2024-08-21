package com.example.productservice.dto;

import com.example.productservice.entities.Category;
import com.example.productservice.entities.ProductImage;
import com.example.productservice.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String name;

    private String description;

    private BigDecimal price;

    private Long categoryId;

    private CategoryDTO category;

    private Set<ProductImageDTO> images;

    private Integer stockQuantity;

    private String manufacturer;

    private String size;

    private String weight;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
}
