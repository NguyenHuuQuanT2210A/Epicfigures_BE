package com.example.orderservice.dto.request;

import com.example.orderservice.enums.ConditionItemStatus;
import com.example.orderservice.enums.ReturnItemStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnItemMail {
    private String email;
    private String username;
    private String orderCode;
    private ReturnItemStatus status;
    private String statusNote;
    private ConditionItemStatus conditionItem;
    private String conditionNote;
    private Integer refundPercentage;
    private BigDecimal refundAmount;
}
