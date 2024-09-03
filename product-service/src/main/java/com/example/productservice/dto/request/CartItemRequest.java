package com.example.productservice.dto.request;

import com.example.productservice.dto.UserAndProductId;
import com.example.productservice.statics.enums.CartStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRequest {
    private UserAndProductId id;
    private Integer quantity;
    private BigDecimal unitPrice;
    @Enumerated(EnumType.STRING)
    private CartStatus status;
}
