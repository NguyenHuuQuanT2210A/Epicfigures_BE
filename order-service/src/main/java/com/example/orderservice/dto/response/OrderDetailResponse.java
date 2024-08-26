package com.example.orderservice.dto.response;

import com.example.common.dto.ProductDTO;
import com.example.orderservice.entities.OrderDetailId;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetailResponse {
    private OrderDetailId id;
    private Integer quantity;
    private BigDecimal unitPrice;
    private ProductDTO productDTO;
}
