package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDetailDTO;
import com.example.orderservice.entities.OrderDetailId;
import com.example.orderservice.service.OrderDetailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "Order", description = "Order Detail Controller")
@CrossOrigin()
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/orderDetail")
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    @GetMapping("/orderAndProduct")
    public ResponseEntity<OrderDetailDTO> getOrderDetailByOrderIdAndProductId(@RequestBody OrderDetailId orderDetailId) {
        OrderDetailDTO orderDetailDTO = orderDetailService.findOrderDetailById(orderDetailId);
        return ResponseEntity.ok().body(orderDetailDTO);
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
    public ResponseEntity<List<OrderDetailDTO>> getOrderDetailsByOrderId(@PathVariable String orderId) {
        List<OrderDetailDTO> orderDetailDTOs = orderDetailService.findOrderDetailByOrderId(orderId);
        return ResponseEntity.ok().body(orderDetailDTOs);
    }

    @PutMapping("/updateQuantity")
    public ResponseEntity<OrderDetailDTO> updateQuantity(@RequestBody OrderDetailId orderDetailId, @RequestParam Integer quantity) {
        OrderDetailDTO orderDetailDTO = orderDetailService.updateQuantity(orderDetailId, quantity);
        return ResponseEntity.ok().body(orderDetailDTO);
    }

    @DeleteMapping("/orderAndProduct")
    public ResponseEntity<String> deleteOrderDetailByOrderIdAndProductId(@RequestBody OrderDetailId orderDetailId) {
        orderDetailService.deleteOrderDetail(orderDetailId);
        return ResponseEntity.ok("Deleted Order Detail");
    }
}
