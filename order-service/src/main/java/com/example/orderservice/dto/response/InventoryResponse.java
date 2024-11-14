package com.example.orderservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    //    private BigDecimal unitPrice;
//    private BigDecimal totalCost;
    private String type;

    private LocalDateTime date;
}
