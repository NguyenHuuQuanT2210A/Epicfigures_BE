package com.example.userservice.dtos.response;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String imageTitle;
    private UserResponse user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
