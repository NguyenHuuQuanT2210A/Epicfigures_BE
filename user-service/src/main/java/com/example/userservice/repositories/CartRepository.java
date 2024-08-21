package com.example.userservice.repositories;

import com.example.userservice.entities.Cart;
import com.example.userservice.entities.UserAndProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, UserAndProductId> {
    Cart findCartById(UserAndProductId userAndProductId);
    List<Cart> findAllByUserId(Long userId);

    @Query("SELECT c FROM Cart c WHERE c.id.productId = :productId")
    List<Cart> findAllByProductId(@Param("productId") Long productId);

    @Modifying
    @Query("DELETE FROM Cart c WHERE c.id.userId IN :user_id")
    void deleteCartsByUserId(@Param("user_id") Long userId);
}
