package com.example.userservice.dtos.request;

import lombok.Getter;

@Getter
public class EmailForgotPassword {
    private String email;
    private String platform;
}
