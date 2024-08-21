package com.example.orderService.request;

import com.example.orderService.entity.OrderItem;
import com.example.orderService.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private Long userId;
    private HashMap<Long,Integer> orderItem; // productId, quantity
    private LocalDate startDate;
    private LocalDate endDate;
}
