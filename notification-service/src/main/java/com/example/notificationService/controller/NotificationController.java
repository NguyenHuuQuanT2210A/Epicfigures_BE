package com.example.notificationService.controller;

import com.example.notificationService.dto.request.NotificationRequest;
import com.example.notificationService.dto.response.ApiResponse;
import com.example.notificationService.dto.response.NotificationResponse;
import com.example.notificationService.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/send-notification")
    void sendNotification(@RequestBody NotificationRequest notification) {
        notificationService.sendNotification(notification);
    }

//    @PostMapping("/send-all-notification")
//    ApiResponse<Void> sendNotificationsAllUser(@RequestBody NotificationRequest notification) {
//        notificationService.sendNotificationsAllUser(notification);
//        return ApiResponse.<Void>builder()
//                .message("Send notifications for all user")
//                .build();
//    }

    @GetMapping("/user/{userId}")
    ApiResponse<List<NotificationResponse>> getNotificationByUserId(@PathVariable Long userId) {
        return ApiResponse.<List<NotificationResponse>>builder()
                .message("Get notifications by user id")
                .data(notificationService.getNotificationsByUserId(userId))
                .build();
    }
}
