package com.example.inventoryservice.dto;

import com.example.inventoryservice.enums.InventoryStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {
    private Long productId;
    private Integer quantity;
    private InventoryStatus status;
    private String reason;
    private String date;
}
