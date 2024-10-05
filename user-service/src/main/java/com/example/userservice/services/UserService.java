package com.example.userservice.services;

import com.example.userservice.dtos.request.UserRequest;
import com.example.userservice.dtos.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserResponse> getAll(Pageable pageable);
    Page<UserResponse> getInTrash(Pageable pageable);
    UserResponse createUser(UserRequest userRequest);
    UserResponse updateUser(Long id, UserRequest userRequest);
    void deleteById(Long id);
    UserResponse findById(Long id);
    UserResponse findByUsername(String username);
    UserResponse findByEmail(String email);
    void moveToTrash(Long id);
    void restoreUser(Long id);
    Page<UserResponse> searchBySpecification(Pageable pageable, String sort, String[] user, String role);

    Object countUsers();

    Page<UserResponse> findByRoleId(Long roleId, Pageable pageable);
}
