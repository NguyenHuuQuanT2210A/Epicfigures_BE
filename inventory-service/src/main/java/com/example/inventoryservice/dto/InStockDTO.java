package com.example.inventoryservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InStockDTO {
    private long product_id;
    private long clinic_id;
    private long stockQuantity;
    private LocalDateTime lastUpdate;
//    private InStockStatus statusStock;
}
