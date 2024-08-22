package com.example.orderservice.dto;

import com.example.orderservice.entities.Order;
import com.example.orderservice.entities.OrderDetailId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDTO {
    @EmbeddedId
    private OrderDetailId id = new OrderDetailId();
    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Order order;
    private Integer quantity;
    private BigDecimal unitPrice;
}
