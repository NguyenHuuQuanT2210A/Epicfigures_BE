package com.example.orderservice.controller;

import com.example.common.enums.OrderSimpleStatus;
import com.example.orderservice.dto.OrderDTO;
import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.specification.SearchBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Order", description = "Order Controller")
@CrossOrigin()
@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ApiResponse<?> getAllOrder(@RequestParam(name = "page") int page, @RequestParam(name = "limit") int limit) {
        return ApiResponse.builder()
                .message("Get All Orders with per page")
                .data(orderService.getAll(PageRequest.of(page - 1, limit)))
                .build();
    }

    @RequestMapping(method = RequestMethod.POST, path = "search")
    public ApiResponse<?> getAllOrders(@RequestBody SearchBody search) {
        return ApiResponse.builder()
                .message("Get All Orders with search body")
                .data(orderService.findAllAndSorting(search))
                .build();
    }

    @RequestMapping(method = RequestMethod.GET, path = "{id}")
    public ApiResponse<?> getOrderById(@PathVariable String id) {
        return ApiResponse.builder()
                .message("Get Order by Id")
                .data(orderService.findById(id))
                .build();
    }

    @GetMapping("user/{id}")
    public ApiResponse<?> getCartByUserId(@PathVariable Long id) {
        return ApiResponse.builder()
                .message("Get Cart by User Id")
                .data(orderService.findCartByUserId(id))
                .build();
    }

    @GetMapping("/myOrder/{orderId}")
    public ApiResponse<?> getProductByOrderId(@PathVariable String orderId) {
        return ApiResponse.builder()
                .message("Get Product By Order Id")
                .data(orderService.findMyOrder(orderId))
                .build();
    }

    @PostMapping("user/{id}")
    public ApiResponse<?> getOrderByUserId(@PathVariable Long id, @RequestBody SearchBody search) {
        return ApiResponse.builder()
                .message("Get Order by User Id")
                .data(orderService.findByUserId(id, search))
                .build();
    }

    @PostMapping()
    public ApiResponse<?> createOrder(@RequestBody OrderDTO order) {
        return ApiResponse.builder()
                .message("Create Order")
                .data(orderService.createOrder(order))
                .build();
    }

    @PutMapping()
    public ApiResponse<?> updateOrder(@RequestBody OrderDTO order) {
        return ApiResponse.builder()
                .message("Update Order")
                .data(orderService.updateOrder(order))
                .build();
    }

    @PutMapping("/changeStatus/{id}")
    public ApiResponse<?> changeStatus(@PathVariable String id, @RequestParam OrderSimpleStatus status) {
        return ApiResponse.builder()
                .message("Change status of Order")
                .data(orderService.changeStatus(id, status))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteOrder(@PathVariable String id) {
        orderService.deleteOrder(id);
        return ApiResponse.builder()
                .message("Delete Order Successfully!")
                .build();
    }
}
