package com.example.inventoryservice.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {
    private Long productId;
    @Min(value = 1, message = "quantity must be greater than or equal to 1")
    @Max(value = 500, message = "quantity must be less than or equal to 500")
    private Integer quantity;
    private BigDecimal unitPrice;
    private Integer inventoryStatusId;
    private String note;
    private String date;
}
