package com.example.notificationService.kafka;

import com.example.notificationService.service.NotificationService;
import com.example.paymentService.event.CreateEventToNotification;
import com.example.paymentService.event.RequestUpdateStatusOrder;
import com.example.userservice.dtos.request.CreateEventToForgotPassword;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {
    private final NotificationService notificationService;

    @KafkaListener(
            topics = "notification",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(CreateEventToNotification orderSendMail){
        log.info(String.format("Event message recieved -> %s", orderSendMail.toString()));
        try {
            notificationService.sendMailOrder(orderSendMail);
            log.info(String.format("Send Email successfully! ", orderSendMail.getEmail()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @KafkaListener(
            topics = "order",
            groupId = "updateStatusOrder"
    )
    public void updateStatusOrder(RequestUpdateStatusOrder requestUpdateStatusOrder){
        log.info(String.format("Update order id -> %s", requestUpdateStatusOrder.getOrderId()));
        try {
            notificationService.consumerUpdateStatusOrder(requestUpdateStatusOrder);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @KafkaListener(
            topics = "forgot-password",
            groupId = "forgotPassword"
    )
    public void forgotPassword(CreateEventToForgotPassword forgotPasswordEvent){
        log.info(String.format("Event message recieved -> %s", forgotPasswordEvent.toString()));
        try {
            notificationService.sendMailForgotPassword(forgotPasswordEvent);
            log.info(String.format("Send Email successfully! ", forgotPasswordEvent.getEmail()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
