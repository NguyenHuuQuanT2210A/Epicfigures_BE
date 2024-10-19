package com.example.productservice.dto.response;

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

    private String codeProduct;

    private String name;

    private String description;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#,###.00")
    private BigDecimal price;

//    private BigDecimal purchasePrice;
//
//    private BigDecimal listPrice;
//
//    private BigDecimal sellingPrice;

    private Long categoryId;

    private CategoryResponse category;

    private Set<ProductImageResponse> images;

    private String manufacturer;

    private String size;

    private String weight;

    private Integer stockQuantity;

    private Integer reservedQuantity;

    private Integer soldQuantity;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime deletedAt;
}
