package com.example.paymentService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {
    private OrderDetailId id = new OrderDetailId();
    private Integer quantity;
    private BigDecimal unitPrice;
    private ProductResponse product;
}
