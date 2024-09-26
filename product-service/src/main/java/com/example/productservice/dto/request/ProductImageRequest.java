package com.example.productservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageRequest {
    private Long imageId;
    private String imageUrl;
    private LocalDateTime createdAt;
}