package com.example.orderservice.dto.request;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CartItemRequest {
    private Long productId;
    private Long userId; // to find shopping cart
    private Integer quantity;
    private BigDecimal unitPrice;
}
