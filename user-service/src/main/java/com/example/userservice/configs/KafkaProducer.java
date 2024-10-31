package com.example.userservice.configs;

import com.example.userservice.dtos.request.ContactRequest;
import com.example.userservice.dtos.request.CreateEventToForgotPassword;
import com.example.userservice.dtos.response.ContactResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {
    private final KafkaTemplate<String, CreateEventToForgotPassword> kafkaTemplate;

    public void sendEmailForgotPassword(CreateEventToForgotPassword event){
        log.info(String.format("Send email to: ", event.getEmail()));
        Message<CreateEventToForgotPassword> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, "forgot-password")
                .build();
        kafkaTemplate.send(message);
    }

    public void sendReplyContact(ContactRequest contactRequest){
        log.info(String.format("Send email to: ", contactRequest.getEmail()));
        Message<ContactRequest> message = MessageBuilder
                .withPayload(contactRequest)
                .setHeader(KafkaHeaders.TOPIC, "contact")
                .build();
        kafkaTemplate.send(message);
    }

}
