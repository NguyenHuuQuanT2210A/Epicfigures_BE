package com.example.notificationService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    private Long userId;
    private String title;
    private String message;
    private String topicRedis;
    private Boolean isSendAll;
    private String type;
    private String url;
}
