package com.example.orderservice.entities;

import com.example.orderservice.entities.base.BaseEntity;
import com.example.common.enums.OrderSimpleStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private Long userId;

    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
    private String country;
    private String postalCode;
    private String note;
    private String paymentMethod;

    private BigDecimal totalPrice;
    @Enumerated(EnumType.ORDINAL)
    private OrderSimpleStatus status;
    @OneToMany(mappedBy = "order",
            fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private Set<OrderDetail> orderDetails;

//    @OneToOne(mappedBy = "order")
//    private OrderInfo orderInfo;

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", totalPrice=" + totalPrice +
                ", status=" + status +
                '}';
    }
}
