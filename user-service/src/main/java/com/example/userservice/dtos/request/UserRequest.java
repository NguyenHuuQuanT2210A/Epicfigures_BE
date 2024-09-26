package com.example.userservice.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String username;
    private String password;
    private String address;
    private String email;
    private String phoneNumber;
    private String gender;
    private String dateOfBirth;
    private String avatar;
    private Set<String> roles;
}