package com.example.orderservice.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private String id;
    private Long userId;
    private BigDecimal totalPrice;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
    private String country;
    private String postalCode;
    private String note;
    private String paymentMethod;

    private String status;
    private String createdAt;
    private String updatedAt;
    private UserResponse user;
    private Set<OrderDetailResponse> orderDetails;
}
