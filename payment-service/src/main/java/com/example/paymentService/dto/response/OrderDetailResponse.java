package com.example.paymentService.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {
    private String id;
    private Integer quantity;
    private Integer returnableQuantity;
    private String unitPrice;
    private String totalPrice;
    private ProductResponse product;
    private String orderId;
    private Long productId;
}
