package com.example.userservice.configs;

import com.example.common.event.CreateEventToForgotPassword;
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

}
