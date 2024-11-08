package com.example.orderservice.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    private Long userId;
    private String title;
    private String message;
    private String topicRedis;
    private String type;
    private String url;
}
