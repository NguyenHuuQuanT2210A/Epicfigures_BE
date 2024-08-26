package com.example.orderservice.dto;

import com.example.common.dto.ProductDTO;
import com.example.orderservice.entities.Order;
import com.example.orderservice.entities.OrderDetailId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private ProductDTO productDTO;
}
