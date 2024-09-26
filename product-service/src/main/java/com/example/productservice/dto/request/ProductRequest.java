package com.example.productservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private Long productId;

    private String name;

    private String description;

    private BigDecimal price;

    private Long categoryId;

    private CategoryRequest category;

    private Set<ProductImageRequest> images;

    private Integer stockQuantity;

    private String manufacturer;

    private String size;

    private String weight;

    private Long soldQuantity;
}
