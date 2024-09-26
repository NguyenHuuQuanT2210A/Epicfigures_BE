package com.example.orderservice.service;

import com.example.orderservice.config.AuthenticationRequestInterceptor;
import com.example.orderservice.dto.response.AddressOrderResponse;
import com.example.orderservice.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "address-order-service", url = "http://localhost:8081/api/v1/address_order",
        configuration = AuthenticationRequestInterceptor.class)
public interface AddressOrderClient {
    @GetMapping("/{id}")
    ApiResponse<AddressOrderResponse> getAddressOrderById(@PathVariable Long id);
}
