package com.example.orderservice.dto.request;

import com.example.orderservice.enums.ReasonReturnItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReturnItemRequest {
    private String orderDetailId;
    private Integer quantityReturned;
    private ReasonReturnItemStatus reason;
    private String reasonNote;
}
