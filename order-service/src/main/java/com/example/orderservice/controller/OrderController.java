package com.example.orderservice.controller;

import com.example.orderservice.enums.OrderSimpleStatus;
import com.example.orderservice.dto.request.OrderRequest;
import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.dto.response.CountOrderByStatus;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.specification.SearchBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Order Controller")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/search-by-specification")
    ApiResponse<?> advanceSearchBySpecification(@RequestParam(defaultValue = "1", name = "page") int page,
                                                @RequestParam(defaultValue = "10", name = "limit") int limit,
                                                @RequestParam(required = false) String sort,
                                                @RequestParam(required = false) String[] order) {
        return ApiResponse.builder()
                .message("List of Orders")
                .data(orderService.searchBySpecification(PageRequest.of(page -1, limit), sort, order))
                .build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ApiResponse<?> getAllOrder(@RequestParam(name = "page") int page, @RequestParam(name = "limit") int limit) {
        return ApiResponse.builder()
                .message("Get All Orders with per page")
                .data(orderService.getAll(PageRequest.of(page - 1, limit)))
                .build();
    }

    @GetMapping("/count")
    public ApiResponse<Long> getCountOrders() {
        return ApiResponse.<Long>builder()
                .message("Get Count Orders")
                .data(orderService.countOrders())
                .build();
    }

    @GetMapping("/count/status")
    public ApiResponse<CountOrderByStatus> getCountByStatusOrder() {
        return ApiResponse.<CountOrderByStatus>builder()
                .message("Get Count By Status Order")
                .data(orderService.getCountByStatusOrder())
                .build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.POST, path = "search")
    public ApiResponse<?> getAllOrders(@RequestBody SearchBody search) {
        return ApiResponse.builder()
                .message("Get All Orders with search body")
                .data(orderService.findAllAndSorting(search))
                .build();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ApiResponse<?> getOrderById(@PathVariable String id) {
        return ApiResponse.builder()
                .message("Get Order by Id")
                .data(orderService.findById(id))
                .build();
    }


    @PostMapping("user/{id}")
    public ApiResponse<?> getOrderByUserId(@PathVariable Long id, @RequestBody SearchBody search) {
        return ApiResponse.builder()
                .message("Get Order by User Id")
                .data(orderService.findByUserId(id, search))
                .build();
    }

    @GetMapping("/user/{userId}/status")
    public ApiResponse<?> getOrderByUserIdAndStatus(@PathVariable Long userId, @RequestParam OrderSimpleStatus status,
                                                    @RequestParam(defaultValue = "1", name = "page") int page,
                                                    @RequestParam(defaultValue = "10", name = "limit") int limit) {
        return ApiResponse.builder()
                .message("Get Order by user Id and status")
                .data(orderService.findOrderByUserIdAndStatus(userId, status, PageRequest.of(page - 1, limit, Sort.Direction.DESC, "createdAt")))
                .build();
    }

    @PostMapping()
    public ApiResponse<?> createOrder(@RequestBody OrderRequest request, HttpServletRequest httpServletRequest) {
        return ApiResponse.builder()
                .message("Create Order")
                .data(orderService.createOrder(request, httpServletRequest))
                .build();
    }

    @PutMapping()
    public ApiResponse<?> updateOrder(@RequestBody OrderRequest request) {
        return ApiResponse.builder()
                .message("Update Order")
                .data(orderService.updateOrder(request))
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

    @GetMapping("products")
    public ApiResponse<?> getProductByOrderId(@RequestParam String orderId) {
        return ApiResponse.builder()
                .message("Get Product By Order Id")
                .data(orderService.findProductsByOrderId(orderId))
                .build();
    }
}
