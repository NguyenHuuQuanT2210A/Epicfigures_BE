package com.example.notificationService.mapper;

import com.example.notificationService.dto.request.NotificationRequest;
import com.example.notificationService.dto.response.NotificationResponse;
import com.example.notificationService.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    Notification toNotification(NotificationRequest notificationRequest);
    NotificationResponse toNotificationResponse(Notification notification);
}
