package com.example.orderService.repository;

import com.example.orderService.entity.ScheduleProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleProductRepository extends JpaRepository<ScheduleProduct,Long> {
    List<ScheduleProduct> findByProductIdAndReservation(Long id, LocalDate localDate);
}
