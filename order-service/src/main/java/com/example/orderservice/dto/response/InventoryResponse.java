package com.example.orderservice.dto.response;

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
    private String type;

    private LocalDateTime date;
}
