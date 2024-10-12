package com.example.productservice.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "firebase")
public class FirebaseStorageProperties {
    private String uploadLogo;

    private String uploadProductImage;

    private String uploadUserImage;
}
