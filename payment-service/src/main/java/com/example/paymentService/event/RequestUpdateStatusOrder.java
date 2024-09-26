package com.example.paymentService.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdateStatusOrder {
    Boolean status;
    String orderId;
}
