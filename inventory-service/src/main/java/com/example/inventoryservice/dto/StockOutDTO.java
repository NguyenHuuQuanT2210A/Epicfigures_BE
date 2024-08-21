package com.example.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockOutDTO {
    private long id;
    private long quantity;
    private long product_id;
    private long clinic_id;
    private String reason;
    private LocalDateTime dateOut;
}
