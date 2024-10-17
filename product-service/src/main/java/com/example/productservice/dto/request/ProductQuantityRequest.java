package com.example.productservice.dto.request;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductQuantityRequest {
    private Long stockQuantity;
    private Long reservedQuantity;
    private Long soldQuantity;
    private Long productId;
}
