package com.example.notificationService.service;

import com.example.notificationService.dto.request.NotificationRequest;
import com.example.notificationService.dto.response.NotificationResponse;

import com.example.notificationService.mapper.NotificationMapper;
import com.example.notificationService.repository.NotificationRepository;
import com.example.orderservice.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisMessageListenerContainer redisMessageListener;
    private final NotificationEventListener notificationEventListener;
    @Value("${redis.pubsub.topic.all}")
    private String topicNotificationAll;
    @Value("${redis.pubsub.topic.user}")
    private String topicNotificationUser;
    @Value("${redis.pubsub.topic.group}")
    private String topicNotificationGroup;

    public void sendNotification(NotificationRequest notificationRequest) {
        var notification = notificationMapper.toNotification(notificationRequest);
        notification.setIsSendAll(Boolean.TRUE.equals(notificationRequest.getIsSendAll()));
        notificationRepository.save(notification);
        var message = notificationMapper.toNotificationResponse(notification);

        if (notificationRequest.getTopicRedis().equals(topicNotificationAll)) {
            sendNotificationsAllUser(message);
        }else if (notificationRequest.getTopicRedis().contains(topicNotificationUser)) {
            sendNotificationToUser(message);
        }

    }

    public void sendNotificationToUser(NotificationResponse message) {
        log.info("Sending WS notification to {} with payload {}", message.getUserId().toString(), message);

        try {
            message.setSendTo("/user/" + message.getUserId() + "/notifications/userTemp");
            redisTemplate.convertAndSend(topicNotificationUser + ":" + message.getUserId(), message);
        } catch (Exception e) {
            throw new CustomException("Error while sending notification use redis to user", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        simpMessagingTemplate.convertAndSendToUser(
                message.getUserId().toString(),
                "/notifications",
                message
        );
    }

    public void sendNotificationsAllUser(NotificationResponse message) {
        log.info("Sending WS notification to all user with payload {}", message);

//            ChannelTopic topic = ChannelTopic.of(notificationRequest.getTopic());
//            redisMessageListener.addMessageListener(notificationEventListener, topic);
//            redisTemplate.convertAndSend(topic.getTopic(), message);

        try {
            message.setSendTo("/topic/allTemp");
            redisTemplate.convertAndSend(topicNotificationAll, message);
        } catch (Exception e) {
            throw new CustomException("Error while sending notification use redis to user", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        simpMessagingTemplate.convertAndSend(
                "/topic/all",
                message
        );
    }

    public List<NotificationResponse> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserIdOrIsSendAllOrderByCreatedAtDesc(userId, true)
                .stream().map(notificationMapper::toNotificationResponse).collect(Collectors.toList());
    }
}

