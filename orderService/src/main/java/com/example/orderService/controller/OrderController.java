package com.example.orderService.controller;

import com.example.orderService.entity.Order;
import com.example.orderService.request.OrderRequest;
import com.example.orderService.service.OrderService;
import com.example.orderService.specification.SearchBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {
    private final OrderService orderService;
    private final RestTemplate restTemplate;

    public OrderController(OrderService orderService, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.orderService = orderService;
    }
//    @RequestMapping(method = RequestMethod.GET, path = "search")
//    public ResponseEntity<?> getAllOrders(@RequestBody SearchBody search) {
//        return ResponseEntity.ok(orderService.findAllAndSorting(search));
//    }

    @RequestMapping(method = RequestMethod.GET, path = "{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest orderRequest) {
        Order order = orderService.createOrder(orderRequest);
        String string = restTemplate.getForObject("http://localhost:8086/api/v1/payment/create_payment?orderId="+ order.getId(),String.class);
        return ResponseEntity.ok(string);
    }

    @PutMapping()
    public ResponseEntity<?> updateOrder(@RequestBody Order order) {
        return ResponseEntity.ok(orderService.updateOrder(order));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.deleteOrder(id));
    }
    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getByUserId(@RequestParam(name = "limit") int limit,
                                        @RequestParam(name = "page") int page,
                                        @PathVariable Long user_id) {
        return ResponseEntity.ok(orderService.findByUser(PageRequest.of(page -1, limit),user_id));
    }
}
