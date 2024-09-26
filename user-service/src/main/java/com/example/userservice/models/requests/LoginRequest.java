package com.example.userservice.models.requests;

import com.example.userservice.statics.enums.Platform;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
