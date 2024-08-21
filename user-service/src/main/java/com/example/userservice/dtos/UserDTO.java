package com.example.userservice.dtos;

import com.example.userservice.entities.Role;
import com.example.userservice.entities.User;
import lombok.*;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String address;
    private String email;
    private String phoneNumber;
    private String dateOfBirth;
    private String gender;
    private String avatar;
    private Set<Role> roles = new HashSet<>();
}