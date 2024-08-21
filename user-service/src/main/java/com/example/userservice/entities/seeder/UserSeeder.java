package com.example.userservice.entities.seeder;

import com.example.userservice.entities.*;
import com.example.userservice.repositories.*;
import com.example.userservice.statics.enums.ERole;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

//@Component
public class UserSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    Faker faker = new Faker();

    public UserSeeder(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) throws Exception {
        createUsers();
    }

    private void createUsers() {
        boolean nameExisting = false;
        Role role = roleRepository.findById(1L).orElseGet(() -> {
            Role newRole = new Role(1, ERole.ROLE_USER);
            return roleRepository.save(newRole);
        });

        Role roleAdmin = roleRepository.findById(2L).orElseGet(() -> {
            Role newRole = new Role(2, ERole.ROLE_ADMIN);
            return roleRepository.save(newRole);
        });

        List<User> users = new ArrayList<>();
        if (role.getId() == 1) {
            for (int i = 0; i < 10; i++) {
                StringBuilder username = new StringBuilder();
                String firstName = faker.name().firstName();

                String lastName = faker.name().lastName();
                username.append(firstName.toLowerCase(Locale.ROOT)).append(lastName.toLowerCase(Locale.ROOT));
                for (User user :
                        users) {
                    if (user.getUsername().contentEquals(username)) {
                        nameExisting = true;
                        break;
                    }
                }
                if (nameExisting) {
                    i--;
                    nameExisting = false;
                    continue;
                }
                String password = encoder.encode("123456");
                String address = faker.address().fullAddress();
                StringBuilder email = new StringBuilder();
                email.append(firstName.toLowerCase(Locale.ROOT)).append(lastName.toLowerCase(Locale.ROOT)).append("@gmail.com");
                String phone = faker.phoneNumber().cellPhone();
                Set<Role> roles = new HashSet<>();
                roles.add(role);
                User user;
                if (i == 0) {
                    username = new StringBuilder("admin");
                    roles.add(roleAdmin);
                    user = new User(username.toString(), password, address, email.toString(), phone, roles);
                } else {
                    user = new User(username.toString(), password, address, email.toString(), phone, roles);
                }
                users.add(user);
            }
        }
        userRepository.saveAll(users);
    }
}