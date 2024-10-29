package com.example.orderservice.dto.request;

import com.example.orderservice.dto.response.OrderResponse;
import com.example.orderservice.entities.Order;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailRequest {
//    private OrderDetailId id = new OrderDetailId();
    private Order order;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private Long productId;
}
