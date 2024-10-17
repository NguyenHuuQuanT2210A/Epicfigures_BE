package com.example.productservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductQuantityResponse {
    private Long id;
    private Long stockQuantity;
    private Long reservedQuantity;
    private Long soldQuantity;
    private Long productId;
}
