package com.example.inventoryservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
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
