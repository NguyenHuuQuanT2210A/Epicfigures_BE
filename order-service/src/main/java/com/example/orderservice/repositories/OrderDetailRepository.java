package com.example.orderservice.repositories;

import com.example.orderservice.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
    List<OrderDetail> findOrderDetailsByOrder_Id(String id);
    OrderDetail findOrderDetailById(String id);

    @Query("SELECT od FROM OrderDetail od WHERE od.order.id = :orderId AND od.productId = :productId")
    OrderDetail findOrderDetailByOrderIdAndProductId(@Param("orderId") String orderId, @Param("productId") Long productId);
}
