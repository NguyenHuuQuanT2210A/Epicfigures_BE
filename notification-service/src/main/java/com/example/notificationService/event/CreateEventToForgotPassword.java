package com.example.notificationService.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventToForgotPassword {
    private long id;
    private String email;
    private String secretKey;
}
