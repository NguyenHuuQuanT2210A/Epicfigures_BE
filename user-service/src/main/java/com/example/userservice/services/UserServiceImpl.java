package com.example.userservice.services;

import com.example.userservice.dtos.UserDTO;
import com.example.userservice.entities.Role;
import com.example.userservice.entities.User;
import com.example.userservice.exceptions.CustomException;
import com.example.userservice.mappers.UserMapper;
import com.example.userservice.models.requests.UserRequest;
import com.example.userservice.repositories.RoleRepository;
import com.example.userservice.repositories.UserRepository;
import com.example.userservice.statics.enums.ERole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public Page<UserDTO> getAll(Pageable pageable) {
        Page<User> userPage = userRepository.findByDeletedAtIsNull(pageable);
        return userPage.map(UserMapper.INSTANCE::userToUserDTO);
    }

    public Page<UserDTO> getInTrash(Pageable pageable) {
        Page<User> userPage = userRepository.findByDeletedAtIsNotNull(pageable);
        return userPage.map(UserMapper.INSTANCE::userToUserDTO);
    }
  
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new CustomException("Cannot find this user id: " + id, HttpStatus.NOT_FOUND);
        }
        return UserMapper.INSTANCE.userToUserDTO(user);
    }

    public UserDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            throw new CustomException("Cannot find this username: " + username, HttpStatus.NOT_FOUND);
        }
        return UserMapper.INSTANCE.userToUserDTO(user);
    }

    @Override
    public UserDTO findByEmail(String email) {
        return UserMapper.INSTANCE.userToUserDTO(userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email)));
    }

    public void moveToTrash(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new CustomException("Cannot find this user id: " + id, HttpStatus.NOT_FOUND);
        }
        LocalDateTime now = LocalDateTime.now();
        user.setDeletedAt(now);

        userRepository.save(user);
    }

    public UserDTO createUser(UserRequest userRequest) {
        User user = new User();
        BeanUtils.copyProperties(userRequest, user);

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new CustomException("Error: Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new CustomException("Error: Email is already in use!", HttpStatus.BAD_REQUEST);
        }

        Set<String> strRoles = userRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new CustomException("Error: Role is not found.", HttpStatus.NOT_FOUND));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new CustomException("Error: Role is not found.", HttpStatus.NOT_FOUND));
                        roles.add(adminRole);
                    }
                    case "mod" -> {
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new CustomException("Error: Role is not found.", HttpStatus.NOT_FOUND));
                        roles.add(modRole);
                    }
                    default -> {
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new CustomException("Error: Role is not found.", HttpStatus.NOT_FOUND));
                        roles.add(userRole);
                    }
                }
            });
        }

        String password = encoder.encode(userRequest.getPassword());
        user.setRoles(roles);
        user.setPassword(password);

        User savedUser = userRepository.save(user);
        return UserMapper.INSTANCE.userToUserDTO(savedUser);
    }

    public UserDTO updateUser(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new CustomException("Cannot find this user id: " + id, HttpStatus.NOT_FOUND);
        }
        var userDTO = findByUsername(userRequest.getUsername());
        if (userDTO != null && !user.getUsername().equals(userRequest.getUsername())) {
            throw new CustomException("User name already exists", HttpStatus.BAD_REQUEST);
        }

        String oldPassword = user.getPassword();

        BeanUtils.copyProperties(userRequest, user);

        Set<String> strRoles = userRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new CustomException("Error: Role is not found.", HttpStatus.NOT_FOUND));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new CustomException("Error: Role is not found.", HttpStatus.NOT_FOUND));
                        roles.add(adminRole);
                    }
                    case "mod" -> {
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new CustomException("Error: Role is not found.", HttpStatus.NOT_FOUND));
                        roles.add(modRole);
                    }
                    default -> {
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new CustomException("Error: Role is not found.", HttpStatus.NOT_FOUND));
                        roles.add(userRole);
                    }
                }
            });
        }

        if (userRequest.getPassword() != null && userRequest.getPassword().length() >= 6){
            user.setPassword(encoder.encode(userRequest.getPassword()));
        } else {
            user.setPassword(oldPassword);
        }

        if (userRequest.getRoles() != null && userRequest.getRoles().size() > 0 ){
            user.setRoles(roles);
        }

        if (userRequest.getAvatar() != null && !Objects.equals(user.getAvatar(),
            userRequest.getAvatar().substring(userRequest.getAvatar().lastIndexOf('/') + 1))){
            user.setAvatar(userRequest.getAvatar());
        }

        User savedUser = userRepository.save(user);
        return UserMapper.INSTANCE.userToUserDTO(savedUser);
    }
  
    public void deleteById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new CustomException("Cannot find this user id: " + id, HttpStatus.NOT_FOUND);
        }

        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomException("Cannot delete this user", HttpStatus.BAD_REQUEST);
        }
    }

}
