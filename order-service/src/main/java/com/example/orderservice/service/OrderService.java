package com.example.orderservice.service;

import com.example.common.dto.OrderDTO;
import com.example.common.dto.ProductDTO;
import com.example.common.enums.OrderSimpleStatus;
import com.example.orderservice.dto.request.OrderRequest;
import com.example.orderservice.specification.SearchBody;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    Page<OrderDTO> getAll(Pageable pageable);
    Page<OrderDTO> findAllAndSorting(SearchBody searchBody);
    OrderDTO findById(String id);
    String createOrder(OrderRequest request);
    Object updateOrder(OrderRequest request);
    Object deleteOrder(String id);
    Page<OrderDTO> findByUserId(Long userId, SearchBody searchBody);
    List<OrderDTO> findByUserId(Long userId);
    OrderDTO findCartByUserId(Long userId);
    OrderDTO changeStatus(String id, OrderSimpleStatus status);
    List<ProductDTO> findProductsByOrderId(String orderId);

    Long countOrders();
}



