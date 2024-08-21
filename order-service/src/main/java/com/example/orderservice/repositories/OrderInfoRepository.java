package com.example.orderservice.repositories;

import com.example.orderservice.entities.OrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderInfoRepository extends JpaRepository<OrderInfo, String> {
    OrderInfo findOrderInfoByOrderId(String orderId);
}
