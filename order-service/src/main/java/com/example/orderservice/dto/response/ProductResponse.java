package com.example.orderservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long productId;

    private String name;

    private String description;

    private String price;

//    private BigDecimal purchasePrice;
//
//    private BigDecimal listPrice;
//
//    private BigDecimal sellingPrice;

    private Long categoryId;

    private CategoryResponse category;

    private String manufacturer;

    private String size;

    private String weight;

    private Integer stockQuantity;

    private Integer reservedQuantity;

    private Integer soldQuantity;

    private Integer returnPeriodDays;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime deletedAt;
    private Set<ProductImageResponse> images;
}
