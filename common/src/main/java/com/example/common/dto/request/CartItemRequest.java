package com.example.common.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartItemRequest {
    private Integer productId;
    private Integer userId; // to find shopping cart
    private Integer quantity;
}
