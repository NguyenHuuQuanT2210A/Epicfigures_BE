package com.example.userservice.dtos.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressOrderRequest {
    private String username;
    private String phone;
    private String addressRegion;
    private String addressDetail;
    private String isDefault;
    private Long userId;
}
