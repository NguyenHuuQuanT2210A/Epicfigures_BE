package com.example.common.dto.response;

import com.example.common.enums.ERole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {
    private long id;
    @Enumerated(EnumType.STRING)
    private ERole name;
}
