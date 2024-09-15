package com.example.common.dto.response;

import com.example.common.enums.ERole;
import jakarta.persistence.*;
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
