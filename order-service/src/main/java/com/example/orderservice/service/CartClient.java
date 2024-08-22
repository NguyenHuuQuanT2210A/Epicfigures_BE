package com.example.orderservice.service;

import com.example.orderservice.dto.request.UserAndProductId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "cart-service", url = "http://localhost:8081/api/v1/cart")
public interface CartClient {
    @DeleteMapping("/ids")
    void deleteByIds(@RequestBody List<UserAndProductId> ids);
}
