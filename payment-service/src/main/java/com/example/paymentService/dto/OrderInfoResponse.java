package com.example.paymentService.dto;

import com.example.common.dto.OrderDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoResponse implements Serializable {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
    private String country;
    private String postalCode;
    private String note;
    private String payment_method;
}
