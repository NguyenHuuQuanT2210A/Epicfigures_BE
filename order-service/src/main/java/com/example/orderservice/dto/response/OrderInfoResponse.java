package com.example.orderservice.dto.response;

import com.example.orderservice.entities.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderInfoResponse implements Serializable {
    String id;
    String firstName;
    String lastName;
    String email;
    String address;
    String phone;
    String country;
    String postalCode;
    String note;

    @JsonIgnore
    Order order;
}
