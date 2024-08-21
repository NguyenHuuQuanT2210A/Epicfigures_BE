package com.example.userservice.controllers;

import com.example.userservice.entities.Role;
import com.example.userservice.services.RoleService;
import com.example.userservice.statics.enums.ERole;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Roles", description = "Role Controller")
@CrossOrigin()
@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/{id}")
    public Optional<Role> getRoleById(long id) {
        return roleService.getRoleById(id);
    }

    @GetMapping("/name/{name}")
    public Optional<Role> getRoleByName(ERole name) {
        return roleService.getRoleByName(name);
    }

    @PostMapping
    public ERole addRole(@RequestParam ERole role) {
        return roleService.addRole(role);
    }

    @PutMapping("/{id}")
    public Role updateRole(long id, Role role) {
        return roleService.updateRole(id, role);
    }

    @DeleteMapping("/{id}")
    public void deleteRole(long id) {
        roleService.deleteRole(id);
    }
}
