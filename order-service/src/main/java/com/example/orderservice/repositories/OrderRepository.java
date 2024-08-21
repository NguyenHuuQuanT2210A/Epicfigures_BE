package com.example.orderservice.repositories;

import com.example.common.enums.OrderSimpleStatus;
import com.example.orderservice.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {
    Page<Order> findAll(Specification specification, Pageable pageable);
    Order findOrderById(String id);
    @Query("SELECT o FROM Order o WHERE o.userId = ?1")
    Page<Order> findOrderByUserId(Long userId, Specification specification, Pageable pageable);
    List<Order> findByUserId(Long userId);
    Order findOrderByUserIdAndStatus(Long userId, OrderSimpleStatus status);
}