package com.example.userservice.repositories;

import com.example.userservice.entities.AddressOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressOrderRepository extends JpaRepository<AddressOrder, Long> {
    List<AddressOrder> findByUserId(Long userId);
}
