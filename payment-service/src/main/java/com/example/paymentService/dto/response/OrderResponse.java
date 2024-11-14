package com.example.paymentService.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
    private UserResponse user;
    private Set<OrderDetailResponse> orderDetails;
}
