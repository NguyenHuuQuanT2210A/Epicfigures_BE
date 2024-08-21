package com.example.orderservice.controller;

import com.example.common.enums.OrderSimpleStatus;
import com.example.orderservice.dto.OrderDTO;
import com.example.orderservice.entities.Order;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.service.OrderServiceImpl;
import com.example.orderservice.specification.SearchBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Order", description = "Order Controller")
@CrossOrigin()
@RestController
@RequestMapping("api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<?> getAllOrder(@RequestParam(name = "page") int page, @RequestParam(name = "limit") int limit) {
        return ResponseEntity.ok(orderService.getAll(PageRequest.of(page - 1, limit)));
    }

    @RequestMapping(method = RequestMethod.POST, path = "search")
    public ResponseEntity<?> getAllOrders(@RequestBody SearchBody search) {
        return ResponseEntity.ok(orderService.findAllAndSorting(search));
    }

    @RequestMapping(method = RequestMethod.GET, path = "{id}")
    public ResponseEntity<?> getOrderById(@PathVariable String id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @GetMapping("user/{id}")
    public ResponseEntity<?> getCartByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findCartByUserId(id));
    }

    @PostMapping("user/{id}")
    public ResponseEntity<?> getOrderByUserId(@PathVariable Long id, @RequestBody SearchBody search) {
        return ResponseEntity.ok(orderService.findByUserId(id, search));
    }

    @PostMapping()
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO order) {
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    @PutMapping()
    public ResponseEntity<?> updateOrder(@RequestBody OrderDTO order) {
        return ResponseEntity.ok(orderService.updateOrder(order));
    }

    @PutMapping("/changeStatus/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable String id, @RequestParam OrderSimpleStatus status) {
        return ResponseEntity.ok(orderService.changeStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable String id) {
        return ResponseEntity.ok(orderService.deleteOrder(id));
    }
}
