package com.example.productservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageResponse {
    private Long imageId;
    private String imageUrl;
    private LocalDateTime createdAt;
}