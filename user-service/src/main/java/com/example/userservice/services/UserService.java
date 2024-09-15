package com.example.userservice.services;

import com.example.common.dto.response.Statistics;
import com.example.userservice.dtos.UserDTO;
import com.example.userservice.models.requests.UserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    Page<UserDTO> getAll(Pageable pageable);
    Page<UserDTO> getInTrash(Pageable pageable);
    UserDTO createUser(UserRequest userRequest);
    UserDTO updateUser(Long id, UserRequest userRequest);
    void deleteById(Long id);
    UserDTO findById(Long id);
    UserDTO findByUsername(String username);
    UserDTO findByEmail(String email);
    void moveToTrash(Long id);
    void restoreUser(Long id);
    Page<UserDTO> searchBySpecification(Pageable pageable, String sort, String[] user, String role);

    Object countUsers();
}
