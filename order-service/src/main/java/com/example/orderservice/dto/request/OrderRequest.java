package com.example.orderservice.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRequest {
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
    private Set<CartItemRequest> cartItems;
}
