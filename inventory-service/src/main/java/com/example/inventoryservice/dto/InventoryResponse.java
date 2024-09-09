package com.example.inventoryservice.dto;

import com.example.inventoryservice.enums.InventoryStatus;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse implements Serializable {
    private Long id;
    private Long productId;
    private Integer quantity;
    private InventoryStatus status;
    private String reason;
    private LocalDateTime date;
}
