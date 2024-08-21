package com.example.orderService.entity;

import com.example.orderService.config.FutureTime;
import com.example.orderService.config.FutureTimeWithReference;
import com.example.orderService.entity.base.BaseEntity;
import com.example.orderService.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "OrderRental")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "order_date")
    private LocalDateTime orderDate;
    @Column(name = "total_amount")
    private Integer totalAmount;
//    @Column(name = "product_id")
//    private Long productId;
    @Column(name = "status")
    private OrderStatus orderStatus;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItem;
}
