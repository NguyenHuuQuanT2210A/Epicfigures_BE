package com.example.inventoryservice.dto;

import com.example.inventoryservice.enums.InventoryStatus;
import com.example.inventoryservice.enums.InventoryType;
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
    private InventoryType type;
    private String date;
}
