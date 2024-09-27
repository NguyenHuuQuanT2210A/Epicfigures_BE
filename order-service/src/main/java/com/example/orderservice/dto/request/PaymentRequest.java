package com.example.orderservice.dto.request;

import com.example.orderservice.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private String orderId;
    private String paymentMethod;
    private PaymentType paymentType;
//    private String baseUrl;
}
