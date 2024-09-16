package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDetailDTO;
import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.entities.OrderDetailId;
import com.example.orderservice.service.OrderDetailService;
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
    public ApiResponse<OrderDetailDTO> getOrderDetailByOrderIdAndProductId(@RequestBody OrderDetailId orderDetailId) {
        OrderDetailDTO orderDetailDTO = orderDetailService.findOrderDetailById(orderDetailId);
        return ApiResponse.<OrderDetailDTO>builder()
                .message("Get Order Detail by OrderId And ProductId")
                .data(orderDetailDTO)
                .build();
    }

    @GetMapping("/isOrderDetailExist")
    public ResponseEntity<Boolean> isOrderDetailExist(@RequestBody OrderDetailId orderDetailId) {
        OrderDetailDTO orderDetailDTO = orderDetailService.findOrderDetailById(orderDetailId);
        if (orderDetailDTO == null) {
            return ResponseEntity.ok(false);
        }else {
            return ResponseEntity.ok(true);
        }
    }

    @GetMapping("/order/{orderId}")
    public ApiResponse<List<OrderDetailDTO>> getOrderDetailsByOrderId(@PathVariable String orderId) {
        List<OrderDetailDTO> orderDetailDTOs = orderDetailService.findOrderDetailByOrderId(orderId);
        return ApiResponse.<List<OrderDetailDTO>>builder()
                .message("Get Order Detail by OrderId")
                .data(orderDetailDTOs)
                .build();
    }

    @PutMapping("/updateQuantity")
    public ApiResponse<OrderDetailDTO> updateQuantity(@RequestBody OrderDetailId orderDetailId, @RequestParam Integer quantity) {
        OrderDetailDTO orderDetailDTO = orderDetailService.updateQuantity(orderDetailId, quantity);
        return ApiResponse.<OrderDetailDTO>builder()
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
