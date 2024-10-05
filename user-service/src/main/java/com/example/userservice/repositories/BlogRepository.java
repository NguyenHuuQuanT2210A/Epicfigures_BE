package com.example.userservice.repositories;

import com.example.userservice.entities.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findBlogByUserIdAndDeletedAtIsNull(Long userId);
    Page<Blog> findByDeletedAtIsNull(Pageable pageable);
    Page<Blog> findByDeletedAtIsNotNull(Pageable pageable);
    @Query("SELECT b FROM Blog b WHERE b.title LIKE %?1% AND b.deletedAt IS NULL")
    Page<Blog> findBlogByTitleLikeAndDeletedAtIsNull(String title, Pageable pageable);
}
