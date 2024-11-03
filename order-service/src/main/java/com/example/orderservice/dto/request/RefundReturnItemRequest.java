package com.example.orderservice.dto.request;

import com.example.orderservice.enums.ConditionItemStatus;
import com.example.orderservice.enums.ReturnItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefundReturnItemRequest {
    private ReturnItemStatus status;
    private ConditionItemStatus conditionItem;
    private String conditionNote;
    private Integer refundPercentage;
    private String isAddStockQty;
}
