package com.example.orderservice.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountOrderByStatus {
    private Long created;
    private Long pending;
    private Long processing;
    private Long onDelivery;
    private Long delivered;
    private Long complete;
    private Long cancel;
}
