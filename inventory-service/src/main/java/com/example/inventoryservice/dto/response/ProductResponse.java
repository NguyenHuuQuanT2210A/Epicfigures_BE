package com.example.inventoryservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long productId;

    private String name;

    private String description;

    private BigDecimal price;

    private Long categoryId;

    private Integer stockQuantity;

    private Integer soldQuantity;

    private String manufacturer;

    private String size;

    private String weight;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
}
