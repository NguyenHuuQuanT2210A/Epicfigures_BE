package com.example.orderservice.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartItemRequest {
    private Long productId;
    private Long clinicId;
    private Long userId; // to find shopping cart
    private Integer quantity;
}
