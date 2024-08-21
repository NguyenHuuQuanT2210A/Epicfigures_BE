package com.example.orderservice.service;

import com.example.orderservice.dto.request.OrderInfoRequest;
import com.example.orderservice.dto.response.OrderInfoResponse;
import com.example.orderservice.entities.Order;

public interface OrderInfoService {
    OrderInfoResponse findById(String id);
    void createOrderInfo(OrderInfoRequest request);
    OrderInfoResponse updateOrderInfo(String id, OrderInfoRequest request);
    OrderInfoResponse findOrderInfoByOrder(Order order);
}
