package com.example.inventoryservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
