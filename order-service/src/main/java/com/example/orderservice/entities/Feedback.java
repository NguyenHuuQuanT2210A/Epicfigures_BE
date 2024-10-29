package com.example.orderservice.entities;

import com.example.orderservice.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "feedback")
public class Feedback extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer rateStar;
    private String comment;

    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_detail_id", updatable = false)
    private OrderDetail orderDetail;
}
