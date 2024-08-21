package com.example.orderService.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    private Long productId;

    private LocalDate reservation;
}
