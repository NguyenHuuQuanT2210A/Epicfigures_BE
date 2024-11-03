package com.example.orderservice.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderDetail {
//    @EmbeddedId
//    private OrderDetailId id = new OrderDetailId();

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Order order;
    private Integer quantity;
    private Integer returnableQuantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private Long productId;

    @OneToOne(mappedBy = "orderDetail")
    private Feedback feedback;

    @OneToMany(mappedBy = "orderDetail", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ReturnItem> returnItem;
}
