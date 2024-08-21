package com.example.paymentService.service;

import com.example.paymentService.dto.OrderInfoRequest;
import com.example.paymentService.dto.OrderInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "orderInfo", url = "http://localhost:8084/api/v1/orderInfo")
public interface OrderInfoClient {
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    void createOrderInfo(@RequestBody OrderInfoRequest request);

}
