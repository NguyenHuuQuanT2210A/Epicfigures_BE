package com.example.userservice.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
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
