package com.example.paymentService.config;


import com.example.paymentService.dto.response.OrderResponse;
import com.example.paymentService.event.CreateEventToNotification;
import com.example.paymentService.event.RequestUpdateStatusOrder;
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
public class KafkaProducer {
    @Value("${spring.kafka.topic.name}")
    private String topicName;
    @Autowired
    private final KafkaTemplate<String, RequestUpdateStatusOrder> kafkaTemplate;
    public KafkaProducer(KafkaTemplate<String, RequestUpdateStatusOrder> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }
    private static final Logger LOGGER = LoggerFactory.getLogger(org.apache.kafka.clients.producer.KafkaProducer.class);
    public void sendMessageStatusOrder(RequestUpdateStatusOrder a){
        LOGGER.info(String.format("Message sent -> %s", a.toString()));

        Message<RequestUpdateStatusOrder> message = MessageBuilder
                .withPayload(a)
                .setHeader(KafkaHeaders.TOPIC, "order")
                .build();
        kafkaTemplate.send(message);
    }

    public void sendEmail(OrderResponse orderResponse){
        LOGGER.info(String.format("Send email to: ", orderResponse.getEmail()));
        Message<OrderResponse> message = MessageBuilder
                .withPayload(orderResponse)
                .setHeader(KafkaHeaders.TOPIC, "notification")
                .build();
        kafkaTemplate.send(message);
    }

}
