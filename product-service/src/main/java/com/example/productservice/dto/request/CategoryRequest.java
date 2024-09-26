package com.example.productservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    private Long categoryId;
    private String categoryName;
    private String description;
    private Long parentCategoryId;
}
