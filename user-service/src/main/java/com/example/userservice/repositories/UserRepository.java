package com.example.userservice.repositories;

import com.example.userservice.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAll(Pageable pageable);

//    @Procedure(procedureName = "find_user_by_username")
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Page<User> findByDeletedAtIsNull(Pageable pageable);
    Page<User> findByDeletedAtIsNotNull(Pageable pageable);
}
