package com.example.orderservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetailResponse {
    private String id;
    private Integer quantity;
    private Integer returnableQuantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private ProductResponse product;
    private String orderId;
    private Long productId;
}
