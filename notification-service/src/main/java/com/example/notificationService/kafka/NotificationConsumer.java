package com.example.notificationService.kafka;

import com.example.notificationService.service.MailSenderService;
import com.example.orderservice.dto.request.ReturnItemMail;
import com.example.paymentService.event.CreateEventToNotification;
import com.example.paymentService.event.RequestUpdateStatusOrder;
import com.example.userservice.dtos.request.ContactRequest;
import com.example.userservice.dtos.request.CreateEventToForgotPassword;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {
    private final MailSenderService mailSenderService;

    @KafkaListener(
            topics = "notification",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(CreateEventToNotification orderSendMail){
        log.info(String.format("Event message recieved -> %s", orderSendMail.toString()));
        try {
            mailSenderService.sendMailOrder(orderSendMail);
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
            mailSenderService.consumerUpdateStatusOrder(requestUpdateStatusOrder);
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
            mailSenderService.sendMailForgotPassword(forgotPasswordEvent);
            log.info(String.format("Send Email successfully! ", forgotPasswordEvent.getEmail()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "contact", groupId = "contact")
    public void replyContact(ContactRequest contactRequest){
        log.info(String.format("Event message recieved -> %s", contactRequest.getUsername()));
        try {
            mailSenderService.sendMailContact(contactRequest);
            log.info(String.format("Send Email successfully! ", contactRequest.getEmail()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "return-item", groupId = "return-item")
    public void returnItem(ReturnItemMail returnItemMail){
        log.info(String.format("Event message recieved -> %s", returnItemMail.getUsername()));
        try {
            mailSenderService.sendMailReturnItem(returnItemMail);
            log.info(String.format("Send Email successfully! ", returnItemMail.getEmail()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
