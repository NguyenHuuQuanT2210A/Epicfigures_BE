package com.example.paymentService.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventToNotification {
    private Long userId;
    private String email;
    private Integer price;
}
