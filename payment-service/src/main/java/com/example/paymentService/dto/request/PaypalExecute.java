package com.example.paymentService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaypalExecute {
    private String paymentId;
    private String payerId;
    private String orderId;
    private String isSuccess;
}
