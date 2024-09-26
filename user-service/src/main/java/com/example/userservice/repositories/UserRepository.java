package com.example.userservice.repositories;

import com.example.userservice.dtos.response.Statistics;
import com.example.userservice.entities.Role;
import com.example.userservice.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Page<User> findAll(Pageable pageable);

    Page<User> findAllByRoles(Set<Role> roles, Pageable pageable);

//    @Procedure(procedureName = "find_user_by_username")
    Optional<User> findByUsername(String username);
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Page<User> findByDeletedAtIsNull(Pageable pageable);
    Page<User> findByDeletedAtIsNotNull(Pageable pageable);

//    @Procedure(procedureName = "get_count_users_by_roles")
    @Query(value = "{call get_count_users_by_roles()}", nativeQuery = true)
    List<Object> getUserCountByRole();
}
