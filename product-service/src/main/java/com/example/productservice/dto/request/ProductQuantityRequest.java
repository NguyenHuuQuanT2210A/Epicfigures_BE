package com.example.productservice.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductQuantityRequest {
    private Integer stockQuantity;
    private Integer reservedQuantity;
    private Integer soldQuantity;
//    private BigDecimal purchasePrice;
}
