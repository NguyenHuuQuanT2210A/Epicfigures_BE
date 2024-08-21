package com.example.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
