package com.example.userservice.dtos.request;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class ResetPasswordDto implements Serializable {
    private String secretKey;
    private String password;
    private String confirmPassword;
}
