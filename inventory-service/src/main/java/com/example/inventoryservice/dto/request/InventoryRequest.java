package com.example.inventoryservice.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {
    private Long productId;
    private Integer quantity;
    private Integer inventoryStatusId;
    private String reason;
    private String date;
}
