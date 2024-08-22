package com.example.userservice.services;

import com.example.userservice.entities.Role;
import com.example.userservice.repositories.RoleRepository;
import com.example.userservice.statics.enums.ERole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> getRoleById(long id) {
        return roleRepository.findById(id);
    }

    public Optional<Role> getRoleByName(ERole name) {
        return roleRepository.findByName(name);
    }

    @Override
    public ERole addRole(ERole role) {
        Role roleNew = new Role();
        roleNew.setName(role);
        roleRepository.save(roleNew);
        return role;
    }

    @Override
    public Role updateRole(long id, Role role) {
        Role roleNew = roleRepository.getOne(id);
        BeanUtils.copyProperties(role, roleNew, "id");

        roleRepository.save(roleNew);
        return role;
    }

    @Override
    public void deleteRole(long id) {
        roleRepository.deleteById(id);
    }
}
