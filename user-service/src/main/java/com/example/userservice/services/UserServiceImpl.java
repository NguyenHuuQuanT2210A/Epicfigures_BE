package com.example.userservice.services;

import com.example.common.dto.response.RoleResponse;
import com.example.common.dto.response.Statistics;
import com.example.userservice.dtos.UserDTO;
import com.example.userservice.entities.Role;
import com.example.userservice.entities.User;
import com.example.userservice.exceptions.CustomException;
import com.example.userservice.mappers.UserMapper;
import com.example.userservice.models.requests.UserRequest;
import com.example.userservice.repositories.RoleRepository;
import com.example.userservice.repositories.UserRepository;
import com.example.userservice.repositories.specification.SpecSearchCriteria;
import com.example.userservice.repositories.specification.UserSpecification;
import com.example.userservice.statics.enums.ERole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.example.userservice.repositories.specification.SearchOperation.OR_PREDICATE_FLAG;
import static com.example.userservice.util.AppConst.SEARCH_SPEC_OPERATOR;
import static com.example.userservice.util.AppConst.SORT_BY;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Override
    public Object countUsers() {
        List<Integer> countUsers = new ArrayList<>();
        List<Object> results = userRepository.getUserCountByRole();

        for (Object result : results) {
            for (Object column : (Object[]) result) {
                System.out.println(column);
                countUsers.add((int) column);
            }
        }
        return new Statistics(countUsers.get(0), countUsers.get(1));
    }

    public Page<UserDTO> getAll(Pageable pageable) {
        Page<User> userPage = userRepository.findByDeletedAtIsNull(pageable);
        return userPage.map(UserMapper.INSTANCE::userToUserDTO);
    }

    public Page<UserDTO> getInTrash(Pageable pageable) {
        Page<User> userPage = userRepository.findByDeletedAtIsNotNull(pageable);
        return userPage.map(UserMapper.INSTANCE::userToUserDTO);
    }
  
    public UserDTO findById(Long id) {
        User user = findUserById(id);
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
        User user = findUserById(id);
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
        User user = findUserById(id);
        if (userRequest.getUsername() != null){
            var userDTO = userRepository.findByUsername(userRequest.getUsername());
            if (userDTO.isPresent() && !user.getUsername().equals(userRequest.getUsername())) {
                throw new CustomException("User name already exists", HttpStatus.BAD_REQUEST);
            }
        }

        if (userRequest.getEmail() != null) {
            var emailDTO = userRepository.findByEmail(userRequest.getEmail());
            if (emailDTO.isPresent() && !userRequest.getEmail().equals(user.getEmail())) {
                throw new CustomException("User already exists with email: " + userRequest.getEmail(), HttpStatus.BAD_REQUEST);
            }
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
        User user = findUserById(id);

        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomException("Cannot delete this user", HttpStatus.BAD_REQUEST);
        }
    }

    private User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new CustomException("Cannot find this user id: " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    public void restoreUser(Long id) {
        User user = findUserById(id);
        user.setDeletedAt(null);
        userRepository.save(user);
    }

    @Override
    public Page<UserDTO> searchBySpecification(Pageable pageable, String sort, String[] user, String role) {
        Pageable pageableSorted = pageable;
        if (StringUtils.hasText(sort)){
            Pattern patternSort = Pattern.compile(SORT_BY);
            Matcher matcher = patternSort.matcher(sort);
            if (matcher.find()) {
                String columnName = matcher.group(1);
                pageableSorted = matcher.group(3).equalsIgnoreCase("desc")
                        ? PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(columnName).descending())
                        : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(columnName).ascending());
            }
        }

        List<SpecSearchCriteria> params = new ArrayList<>();
        Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);
        if (user != null) {
            params.addAll(parseUserCriteria(user, pattern));
        }
        if (role != null) {
            params.addAll(parseRoleCriteria(role, pattern));
        }

        if (params.isEmpty()) {
            return userRepository.findAll(pageableSorted).map(UserMapper.INSTANCE::userToUserDTO);
        }

        Specification<User> result = new UserSpecification(params.get(0));
        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).getOrPredicate()
                    ? Specification.where(result).or(new UserSpecification(params.get(i)))
                    : Specification.where(result).and(new UserSpecification(params.get(i)));
        }

        Page<User> products = userRepository.findAll(Objects.requireNonNull(result), pageableSorted);
        return products.map(UserMapper.INSTANCE::userToUserDTO);
    }

    private List<SpecSearchCriteria> parseUserCriteria(String[] user, Pattern pattern) {
        List<SpecSearchCriteria> params = new ArrayList<>();
        for (String u : user) {
            Matcher matcher = pattern.matcher(u);
            if (matcher.find()) {
                SpecSearchCriteria searchCriteria = new SpecSearchCriteria(null, matcher.group(2), matcher.group(4), matcher.group(6), matcher.group(1), matcher.group(3), matcher.group(5));
                if (u.startsWith(OR_PREDICATE_FLAG)) {
                    searchCriteria.setOrPredicate(true);
                }
                params.add(searchCriteria);
            }
        }
        return params;
    }

    private List<SpecSearchCriteria> parseRoleCriteria(String role, Pattern pattern) {
        List<SpecSearchCriteria> params = new ArrayList<>();
        Matcher matcher = pattern.matcher(role);
        if (matcher.find()) {
            SpecSearchCriteria searchCriteria = new SpecSearchCriteria(null, matcher.group(2), matcher.group(4), matcher.group(6), matcher.group(1), matcher.group(3), matcher.group(5));
            if (role.startsWith(OR_PREDICATE_FLAG)){
                searchCriteria.setOrPredicate(true);
            }
            params.add(searchCriteria);
        }
        return params;
    }

}
