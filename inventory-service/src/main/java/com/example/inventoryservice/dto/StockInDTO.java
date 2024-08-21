package com.example.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockInDTO {
    private long id;
    private long product_id;
    private long clinic_id;
    private long quantity;
    private LocalDateTime dateIn;
    private String supplier;
    private LocalDateTime manufactureDate;
    private LocalDateTime expiryDate;
    private int status;
}
