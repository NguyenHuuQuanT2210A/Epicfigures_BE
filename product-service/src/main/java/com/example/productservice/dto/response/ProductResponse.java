package com.example.productservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long productId;

    private String name;

    private String description;

    private BigDecimal price;

    private Long categoryId;

    private CategoryResponse category;

    private Set<ProductImageResponse> images;

    private Integer stockQuantity;

    private String manufacturer;

    private String size;

    private String weight;

    private Long soldQuantity;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
}
