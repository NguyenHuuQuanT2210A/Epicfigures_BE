package com.example.productservice.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {
    private Long categoryId;
    private String categoryName;
    private String description;
    private Long parentCategoryId;
}
