package com.example.orderservice.service;

import com.example.orderservice.dto.response.OrderResponse;
import com.example.orderservice.dto.response.ProductResponse;
import com.example.orderservice.enums.OrderSimpleStatus;
import com.example.orderservice.dto.request.OrderRequest;
import com.example.orderservice.dto.response.CountOrderByStatus;
import com.example.orderservice.specification.SearchBody;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    Page<OrderResponse> getAll(Pageable pageable);
    Page<OrderResponse> findAllAndSorting(SearchBody searchBody);
    OrderResponse findById(String id);
    String createOrder(OrderRequest request, HttpServletRequest httpServletRequest);
    Object updateOrder(OrderRequest request);
    Object deleteOrder(String id);
    Page<OrderResponse> findByUserId(Long userId, SearchBody searchBody);
    List<OrderResponse> findByUserId(Long userId);
    OrderResponse changeStatus(String id, OrderSimpleStatus status);
    List<ProductResponse> findProductsByOrderId(String orderId);

    Long countOrders();
    Page<OrderResponse> searchBySpecification(Pageable pageable, String sort, String[] order);

    Page<OrderResponse> findOrderByUserIdAndStatus(Long userId, OrderSimpleStatus status, Pageable pageable);

    CountOrderByStatus getCountByStatusOrder();

    OrderResponse changePaymentMethod(String id, String paymentMethod);

    OrderResponse findByCode(String code);
}



