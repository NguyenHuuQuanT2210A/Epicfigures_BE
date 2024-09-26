package com.example.paymentService.dto.response;

import com.example.paymentService.enums.ERole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {
    private long id;
    @Enumerated(EnumType.STRING)
    private ERole name;
}
