package com.example.userservice.controllers;

import com.example.userservice.dtos.response.ApiResponse;
import com.example.userservice.entities.Role;
import com.example.userservice.services.RoleService;
import com.example.userservice.statics.enums.ERole;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
@Tag(name = "Roles", description = "Role Controller")
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public ApiResponse<List<Role>> getAllRoles() {
        return ApiResponse.<List<Role>>builder()
                .message("Get all role")
                .data(roleService.getAllRoles())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<Optional<Role>> getRoleById(long id) {
        return ApiResponse.<Optional<Role>>builder()
                .message("Get role by Id")
                .data(roleService.getRoleById(id))
                .build();
    }

    @GetMapping("/name/{name}")
    public ApiResponse<Optional<Role>> getRoleByName(ERole name) {
        return ApiResponse.<Optional<Role>>builder()
                .message("Get role by Name")
                .data(roleService.getRoleByName(name))
                .build();
    }

    @PostMapping
    public ApiResponse<ERole> addRole(@RequestParam ERole role) {
        return ApiResponse.<ERole>builder()
                .message("Create Role")
                .data(roleService.addRole(role))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<Role> updateRole(long id, Role role) {
        return ApiResponse.<Role>builder()
                .message("Update Role")
                .data(roleService.updateRole(id, role))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteRole(long id) {
        roleService.deleteRole(id);
        return ApiResponse.<String>builder()
                .message("Delete Role Successfully")
                .build();
    }
}
