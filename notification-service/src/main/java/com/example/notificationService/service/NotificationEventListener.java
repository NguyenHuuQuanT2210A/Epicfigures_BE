package com.example.notificationService.service;

import com.example.notificationService.dto.response.NotificationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationEventListener implements MessageListener {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            log.info("New message received: {}", message);
            var notificationResponse = objectMapper.readValue(message.getBody(), NotificationResponse.class);

            simpMessagingTemplate.convertAndSend(
                    notificationResponse.getSendTo(),
                    notificationResponse
            );
        } catch (Exception e) {
            log.error("Error while parsing message");
        }
    }
}
