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
    @JoinColumns({
            @JoinColumn(name = "order_id", updatable = false),
            @JoinColumn(name = "product_id", updatable = false),
    })
    private OrderDetail orderDetail;
}
