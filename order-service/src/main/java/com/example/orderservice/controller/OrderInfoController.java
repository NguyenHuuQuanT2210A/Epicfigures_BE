package com.example.orderservice.controller;

import com.example.orderservice.dto.request.OrderInfoRequest;
import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.dto.response.FeedbackResponse;
import com.example.orderservice.dto.response.OrderInfoResponse;
import com.example.orderservice.entities.Order;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.service.OrderInfoService;
import com.example.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Order", description = "Order Controller")
@CrossOrigin()
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/orderInfo")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderInfoController {
    OrderInfoService orderInfoService;
    OrderService orderService;
    OrderMapper orderMapper;

    @GetMapping("/{id}")
    ApiResponse<OrderInfoResponse> getOrderInfoById(@PathVariable String id) {
        return ApiResponse.<OrderInfoResponse>builder()
                .message("Get Order Info By Id")
                .result(orderInfoService.findById(id))
                .build();
    }

    @GetMapping("order/{orderId}")
    ApiResponse<OrderInfoResponse> getOrderInfoByOrderId(@PathVariable String orderId) {
        Order order = orderMapper.orderDTOToOrder(orderService.findById(orderId));
        return ApiResponse.<OrderInfoResponse>builder()
                .message("Get Order Info By Order Id")
                .result(orderInfoService.findOrderInfoByOrder(order))
                .build();
    }

    @PostMapping
    ApiResponse<String> createOrderInfo(@RequestBody OrderInfoRequest request) {
        orderInfoService.createOrderInfo(request);
        return ApiResponse.<String>builder()
                .code(201)
                .message("Created Order Info")
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<OrderInfoResponse> updateOrderInfo(@PathVariable String id, @RequestBody OrderInfoRequest request) {
        return ApiResponse.<OrderInfoResponse>builder()
                .message("Updated Order Info")
                .result(orderInfoService.updateOrderInfo(id, request))
                .build();
    }
}
