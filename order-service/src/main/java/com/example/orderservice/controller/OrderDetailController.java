package com.example.orderservice.controller;

import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.dto.response.OrderDetailResponse;
import com.example.orderservice.entities.OrderDetailId;
import com.example.orderservice.service.impl.OrderDetailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orderDetail")
@Tag(name = "Order", description = "Order Detail Controller")
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    @GetMapping("/orderAndProduct")
    public ApiResponse<OrderDetailResponse> getOrderDetailByOrderIdAndProductId(@RequestBody OrderDetailId orderDetailId) {
        OrderDetailResponse orderDetailDTO = orderDetailService.findOrderDetailById(orderDetailId);
        return ApiResponse.<OrderDetailResponse>builder()
                .message("Get Order Detail by OrderId And ProductId")
                .data(orderDetailDTO)
                .build();
    }

    @GetMapping("/isOrderDetailExist")
    public ResponseEntity<Boolean> isOrderDetailExist(@RequestBody OrderDetailId orderDetailId) {
        OrderDetailResponse orderDetailDTO = orderDetailService.findOrderDetailById(orderDetailId);
        if (orderDetailDTO == null) {
            return ResponseEntity.ok(false);
        }else {
            return ResponseEntity.ok(true);
        }
    }

    @GetMapping("/order/{orderId}")
    public ApiResponse<List<OrderDetailResponse>> getOrderDetailsByOrderId(@PathVariable String orderId) {
        List<OrderDetailResponse> orderDetailDTOs = orderDetailService.findOrderDetailByOrderId(orderId);
        return ApiResponse.<List<OrderDetailResponse>>builder()
                .message("Get Order Detail by OrderId")
                .data(orderDetailDTOs)
                .build();
    }

    @PutMapping("/updateQuantity")
    public ApiResponse<OrderDetailResponse> updateQuantity(@RequestBody OrderDetailId orderDetailId, @RequestParam Integer quantity) {
        OrderDetailResponse orderDetailDTO = orderDetailService.updateQuantity(orderDetailId, quantity);
        return ApiResponse.<OrderDetailResponse>builder()
                .message("Update quantity of Order Detail")
                .data(orderDetailDTO)
                .build();
    }

    @DeleteMapping("/orderAndProduct")
    public ApiResponse<String> deleteOrderDetailByOrderIdAndProductId(@RequestBody OrderDetailId orderDetailId) {
        orderDetailService.deleteOrderDetail(orderDetailId);
        return ApiResponse.<String>builder()
                .message("Delete Order Detail Successfully!")
                .build();
    }
}
