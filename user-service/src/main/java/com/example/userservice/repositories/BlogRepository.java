package com.example.userservice.repositories;

import com.example.userservice.entities.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findBlogByUserId(Long userId);
    Page<Blog> findByDeletedAtIsNull(Pageable pageable);
    Page<Blog> findByDeletedAtIsNotNull(Pageable pageable);
}
