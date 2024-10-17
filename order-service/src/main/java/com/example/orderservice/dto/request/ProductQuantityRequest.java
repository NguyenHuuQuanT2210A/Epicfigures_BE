package com.example.orderservice.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductQuantityRequest {
    private Long stockQuantity;
    private Long reservedQuantity;
    private Long soldQuantity;
}
