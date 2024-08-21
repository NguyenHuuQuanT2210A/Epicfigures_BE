package com.example.orderservice.dto.response;

import com.example.orderservice.entities.OrderDetail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackResponse {
    Long id;
    Integer rateStar;
    String comment;
    @JsonIgnore
    OrderDetail orderDetail;
}
