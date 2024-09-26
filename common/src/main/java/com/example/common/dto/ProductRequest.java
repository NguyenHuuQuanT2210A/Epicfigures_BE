package com.example.common.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private Long productId;
    private String name;
    private BigDecimal price;

    public ProductRequest(long productId, String name, int i) {
        this.productId = productId;
        this.name = name;
        this.price = BigDecimal.valueOf(i);
    }

    @Override
    public String toString() {
        return "ProductRequest{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
