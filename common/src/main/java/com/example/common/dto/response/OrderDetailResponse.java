package com.example.common.dto.response;

import com.example.common.dto.OrderDetailId;
import com.example.common.dto.ProductDTO;
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
    private ProductDTO product;
}
