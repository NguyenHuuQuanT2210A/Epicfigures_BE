package com.example.orderService.dto;

import com.example.orderService.config.FutureTime;
import com.example.orderService.config.FutureTimeWithReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private Long userId;
    private LocalDateTime orderDate;
    private Integer totalAmount;
    private Long productId;
    @FutureTime
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
