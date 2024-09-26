package com.example.productservice.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageResponse {
    private Long imageId;
    private String imageUrl;
    private LocalDateTime createdAt;
}