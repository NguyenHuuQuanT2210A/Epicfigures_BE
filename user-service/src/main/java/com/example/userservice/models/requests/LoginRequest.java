package com.example.userservice.models.requests;

import com.example.userservice.statics.enums.Platform;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotNull(message = "platform must be not null")
    @Enumerated(EnumType.STRING)
    private Platform platform;

    private String version;
}
