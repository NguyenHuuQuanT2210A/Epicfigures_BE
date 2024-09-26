package com.example.productservice.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageRequest {
    private Long imageId;
    private String imageUrl;
    private LocalDateTime createdAt;
}