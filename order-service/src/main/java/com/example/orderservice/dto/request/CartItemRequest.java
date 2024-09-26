package com.example.orderservice.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequest {
    private Long productId;
    private Long userId; // to find shopping cart
    private Integer quantity;
    private BigDecimal unitPrice;
}
