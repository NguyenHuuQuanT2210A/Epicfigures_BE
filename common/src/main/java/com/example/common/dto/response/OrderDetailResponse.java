package com.example.common.dto.response;

import com.example.common.dto.OrderDetailId;
import com.example.common.dto.ProductDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetailResponse {
    private OrderDetailId id;
    private Integer quantity;
    private BigDecimal unitPrice;
    private ProductDTO product;
}
