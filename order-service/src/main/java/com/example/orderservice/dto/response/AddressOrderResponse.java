package com.example.orderservice.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressOrderResponse {
    private Long id;
    private String username;
    private String phone;
    private String addressRegion;
    private String addressDetail;
    private String isDefault;
}
