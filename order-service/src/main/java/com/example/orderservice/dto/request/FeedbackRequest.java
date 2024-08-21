package com.example.orderservice.dto.request;

import com.example.orderservice.entities.OrderDetail;
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
    String comment;
    OrderDetail orderDetail;
}
