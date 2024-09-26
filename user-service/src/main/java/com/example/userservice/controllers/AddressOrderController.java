package com.example.userservice.controllers;

import com.example.userservice.dtos.request.AddressOrderRequest;
import com.example.userservice.dtos.response.AddressOrderResponse;
import com.example.userservice.dtos.response.ApiResponse;
import com.example.userservice.services.AddressOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/address_order")
@Tag(name = "Address Order", description = "Address Order Controller")
public class AddressOrderController {
    private final AddressOrderService addressOrderService;

    @GetMapping("/user/{userId}")
    public ApiResponse<List<AddressOrderResponse>> getAllAddressOrderByUserId(@PathVariable Long userId) {
        return ApiResponse.<List<AddressOrderResponse>>builder()
                .message("Get all Address Order By UserId")
                .data(addressOrderService.getAddressOrdersByUserId(userId))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<AddressOrderResponse> getAddressOrderById(@PathVariable Long id) {
        return ApiResponse.<AddressOrderResponse>builder()
                .message("Get Address Order by Id")
                .data(addressOrderService.getAddressOrderById(id))
                .build();
    }

    @PostMapping
    public ApiResponse<AddressOrderResponse> addAddressOrder(@RequestBody AddressOrderRequest request) {
        return ApiResponse.<AddressOrderResponse>builder()
                .message("Create Address Order")
                .data(addressOrderService.addAddressOrder(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<AddressOrderResponse> updateAddressOrder(@PathVariable Long id, @RequestBody AddressOrderRequest request) {
        return ApiResponse.<AddressOrderResponse>builder()
                .message("Update Address Order")
                .data(addressOrderService.updateAddressOrder(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteAddressOrder(@PathVariable Long id) {
        addressOrderService.deleteAddressOrder(id);
        return ApiResponse.<String>builder()
                .message("Delete Address Order Successfully")
                .build();
    }
}
