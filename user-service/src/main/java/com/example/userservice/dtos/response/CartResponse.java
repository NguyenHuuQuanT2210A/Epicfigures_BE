package com.example.userservice.dtos.response;

import com.example.userservice.entities.UserAndProductId;
import com.example.userservice.statics.enums.CartStatus;
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
    BigDecimal unitPrice;
    String productName;
    BigDecimal productPrice;
    String description;
    CartStatus status;
    Set<String> productImages;
}
