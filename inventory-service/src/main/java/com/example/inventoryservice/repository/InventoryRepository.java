package com.example.inventoryservice.repository;

import com.example.inventoryservice.entities.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findInventoryByProductId(Long productId);
    Page<Inventory> findByDeletedAtIsNull(Pageable pageable);
    Page<Inventory> findByDeletedAtIsNotNull(Pageable pageable);
}
