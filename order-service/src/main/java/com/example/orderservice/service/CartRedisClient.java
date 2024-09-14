package com.example.orderservice.service;

import com.example.orderservice.config.AuthenticationRequestInterceptor;
import com.example.orderservice.dto.request.UserAndProductId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "cart-redis-service", url = "http://localhost:8082/api/v1/cart",
        configuration = { AuthenticationRequestInterceptor.class })
public interface CartRedisClient {
    @DeleteMapping("/ids")
    void deleteByIds(@RequestBody List<UserAndProductId> ids);
}
