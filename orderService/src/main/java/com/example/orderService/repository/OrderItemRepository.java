package com.example.orderService.repository;

import com.example.orderService.entity.OrderItem;
import com.example.orderService.entity.OrderItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {
}
