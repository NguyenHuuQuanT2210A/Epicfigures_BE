package com.example.orderservice.service;

import com.example.orderservice.dto.request.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", url = "http://localhost:8087/api/v1/notification")
public interface NotificationClient {
    @PostMapping("/send-notification")
    void sendNotification(@RequestBody NotificationRequest notificationRequest);
}
