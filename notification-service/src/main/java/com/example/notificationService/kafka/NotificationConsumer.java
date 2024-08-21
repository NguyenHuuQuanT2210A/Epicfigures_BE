package com.example.notificationService.kafka;

import com.example.common.event.CreateEventToNotification;
import com.example.notificationService.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {
    public static final Logger LOGGER = LoggerFactory.getLogger(NotificationConsumer.class);

    @Autowired
    private NotificationService notificationService;

    @KafkaListener(
            topics = "notification"
            ,
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(CreateEventToNotification orderSendMail){
        LOGGER.info(String.format("Event message recieved -> %s", orderSendMail.toString()));
        try {
            notificationService.sendMailOrder(orderSendMail);
            LOGGER.info(String.format("Send Email successfully! ", orderSendMail.getEmail()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
