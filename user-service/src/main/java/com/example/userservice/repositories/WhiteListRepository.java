package com.example.userservice.repositories;

import com.example.userservice.entities.UserAndProductId;
import com.example.userservice.entities.WhiteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WhiteListRepository extends JpaRepository<WhiteList, UserAndProductId> {
    List<WhiteList> findAllByUserId(Long userId);

    @Query("SELECT w FROM WhiteList w WHERE w.id.productId = :productId")
    List<WhiteList> findAllByProductId(@Param("productId") Long productId);
}
