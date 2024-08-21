package com.example.orderservice.dto.request;

import com.example.orderservice.entities.Order;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderInfoRequest implements Serializable {
    String firstName;
    String lastName;
    String email;
    String address;
    String phone;
    String country;
    String postalCode;
    String note;

    Order order;
}
