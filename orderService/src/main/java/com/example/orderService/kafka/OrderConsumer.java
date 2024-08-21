package com.example.orderService.kafka;


import com.example.common.event.RequestUpdateStatusOrder;
import com.example.orderService.service.OrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {
    public static final Logger LOGGER = LoggerFactory.getLogger(OrderConsumer.class);

    @Autowired
    private OrderService orderService;

    @KafkaListener(
            topics = "order"
            ,
            groupId = "orderId"
    )
    public void consume(RequestUpdateStatusOrder requestUpdateStatusOrder){
        LOGGER.info(String.format("Event message recieved -> %s", requestUpdateStatusOrder.toString()));
        try {
            orderService.updateOrderStatus(requestUpdateStatusOrder.getStatus(), requestUpdateStatusOrder.getOrderId());
            LOGGER.info(String.format("Update successfully!"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
