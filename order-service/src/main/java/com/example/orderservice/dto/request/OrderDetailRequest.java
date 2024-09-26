package com.example.orderservice.dto.request;

import com.example.orderservice.dto.response.OrderResponse;
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
public class OrderDetailRequest {
    private OrderDetailId id = new OrderDetailId();
    private OrderResponse order;
    private Integer quantity;
    private BigDecimal unitPrice;
}
