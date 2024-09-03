package com.example.productservice.dto.response;

import com.example.productservice.dto.UserAndProductId;
import com.example.productservice.statics.enums.CartStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
    private UserAndProductId id;
    private Integer quantity;
    private BigDecimal unitPrice;
    private String productName;
    private BigDecimal productPrice;
    private String description;
    private CartStatus status;
    private Set<String> productImages;
}
