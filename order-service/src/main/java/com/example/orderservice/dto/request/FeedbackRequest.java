package com.example.orderservice.dto.request;

import com.example.orderservice.entities.OrderDetail;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackRequest {
    Integer rateStar;
    @NotBlank(message = "Comment cannot be empty")
    @Size(max = 500, message = "Comment cannot be longer than 500 characters")
    String comment;
    OrderDetail orderDetail;
}
