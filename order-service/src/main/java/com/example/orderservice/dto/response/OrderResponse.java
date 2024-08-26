package com.example.orderservice.dto.response;

import com.example.common.enums.OrderSimpleStatus;
import com.example.orderservice.dto.OrderDetailDTO;
import com.example.orderservice.dto.UserDTO;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
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
    private String payment_method;

    private BigDecimal totalPrice;
    @Enumerated(EnumType.ORDINAL)
    private OrderSimpleStatus status;

    private String createdAt;
    private String updatedAt;
    private UserDTO user;
    private Set<OrderDetailResponse> orderDetails;
}
