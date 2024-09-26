package com.example.userservice.models.response;

import com.example.userservice.statics.enums.Platform;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
    @Builder.Default
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private Platform platform;
    private String version;
    private List<String> roles;
}
