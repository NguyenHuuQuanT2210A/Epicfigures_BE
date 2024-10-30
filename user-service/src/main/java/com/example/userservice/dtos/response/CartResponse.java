package com.example.userservice.dtos.response;

import com.example.userservice.entities.UserAndProductId;
import com.example.userservice.statics.enums.CartStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartResponse {
    UserAndProductId id;
    Integer quantity;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#,###.00")
    BigDecimal unitPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#,###.00")
    BigDecimal totalPrice;
    String productName;
    String productPrice;
    String description;
    CartStatus status;
    Set<String> productImages;
}
