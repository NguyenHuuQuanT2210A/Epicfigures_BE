package com.example.paymentService.event;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCreatedEvent {
    private Long userId;
    private String email;
    private Integer price;
}
