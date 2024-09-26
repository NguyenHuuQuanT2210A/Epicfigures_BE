package com.example.orderservice.dto.response;

import com.example.orderservice.entities.OrderDetailId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailResponse {
    private OrderDetailId id = new OrderDetailId();
    private Integer quantity;
    private BigDecimal unitPrice;
    private ProductResponse product;
}
