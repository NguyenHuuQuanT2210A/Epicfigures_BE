package com.example.orderservice.config;


import com.example.orderservice.dto.request.ReturnItemMail;
import com.example.orderservice.dto.request.ReturnItemStatusRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducer {
    @Autowired
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, Object> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessageReturnItem(ReturnItemMail returnItemMail){
        log.info(String.format("Message sent -> %s", returnItemMail.toString()));

        Message<ReturnItemMail> message = MessageBuilder
                .withPayload(returnItemMail)
                .setHeader(KafkaHeaders.TOPIC, "return-item")
                .build();
        kafkaTemplate.send(message);
    }
}
