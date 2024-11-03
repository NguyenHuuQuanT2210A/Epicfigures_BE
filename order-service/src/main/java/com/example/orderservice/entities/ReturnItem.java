package com.example.orderservice.entities;

import com.example.orderservice.entities.base.BaseEntity;
import com.example.orderservice.enums.ConditionItemStatus;
import com.example.orderservice.enums.ReasonReturnItemStatus;
import com.example.orderservice.enums.ReturnItemStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "return_item")
public class ReturnItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantityReturned;

    @Enumerated(EnumType.STRING)
    private ReasonReturnItemStatus reason;
    private String reasonNote;

    @Enumerated(EnumType.STRING)
    private ReturnItemStatus status;
    private String statusNote;

    @Enumerated(EnumType.STRING)
    private ConditionItemStatus conditionItem;
    private String conditionNote;

    private Integer refundPercentage;
    private BigDecimal refundAmount;

    @Column(length = 2500)
    private String images;

    @ManyToOne
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;
}
