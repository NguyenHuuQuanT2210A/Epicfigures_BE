package com.example.inventoryservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {
    private Long productId;
    private Integer quantity;
    private String type;
    private String date;
}
