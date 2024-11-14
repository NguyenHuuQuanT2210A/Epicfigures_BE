package com.example.orderservice.dto.response;

import com.example.orderservice.enums.ConditionItemStatus;
import com.example.orderservice.enums.ReasonReturnItemStatus;
import com.example.orderservice.enums.ReturnItemStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReturnItemResponse {
    private Long id;
    private String username;
    private Integer quantityReturned;

    private ReasonReturnItemStatus reason;
    private String reasonNote;

    private ReturnItemStatus status;
    private String statusNote;

    private ConditionItemStatus conditionItem;
    private String conditionNote;

    private Integer refundPercentage;
    private BigDecimal refundAmount;
//    private String orderDetailId;
    private OrderDetailResponse orderDetail;
    private List<String> images;
}
